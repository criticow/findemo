package com.waddahex.financeiroapi.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.waddahex.financeiroapi.dto.*;
import com.waddahex.financeiroapi.model.User;
import com.waddahex.financeiroapi.repository.UserRepository;
import com.waddahex.financeiroapi.security.JwtService;

@Service
public class AuthService {
  @Value("${spring.mail.username}")
  private String from;

  @Value("${frontend.url}")
  private String frontendURL;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private EmailService emailService;

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public ResponseEntity<?> signin(SigninupRequest loginRequest) {
    CustomResponse<Void> response = new CustomResponse<>();
    String email = loginRequest.getEmail();
    String password = loginRequest.getPassword();

    Optional<User> existingUser = userRepository.findByEmail(email);

    response.setMessage("Email ou senha inválidos!");
    if (existingUser.isEmpty() || !passwordEncoder.matches(password, existingUser.get().getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    String token = jwtService.generateToken(existingUser.get().getEmail());
    response.setMessage("Usuário logado com sucesso!");

    return ResponseEntity.status(HttpStatus.CREATED)
      .header("token", token)
      .body(response);
  }

  public ResponseEntity<?> signup(SigninupRequest signupRequest) {
    CustomResponse<Void> response = new CustomResponse<>();

    if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
      response.setMessage("Já existe usuário com esse email!");
      return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    String hashedPassword = passwordEncoder.encode(signupRequest.getPassword());

    User user = new User();
    user.setEmail(signupRequest.getEmail());
    user.setPassword(hashedPassword);

    User savedUser = userRepository.save(user);

    if (savedUser == null) {
      response.setMessage("Erro inesperado ao tentar criar usuário!");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    String token = jwtService.generateToken(savedUser.getEmail());
    response.setMessage("Usuário criado com sucesso!");

    return ResponseEntity.status(HttpStatus.CREATED)
      .header("token", token)
      .body(response);
  }

  public ResponseEntity<?> forgotPassword(ForgotPasswordRequest request) {
    CustomResponse<Object> response = new CustomResponse<>();

    Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

    if (existingUser.isPresent()) {
      User user = existingUser.get();
      String resetToken = UUID.randomUUID().toString();
      user.setPasswordResetToken(resetToken);
      userRepository.save(user);

      String subject = "Redefinição de Senha";
      String resetLink = frontendURL + "/reset-password?token=" + resetToken;

      String htmlContent =
        "<html><body>" +
        "<h2>Redefinição de Senha</h2>" +
        "<p>Olá,</p>" +
        "<p>Recebemos uma solicitação para redefinir sua senha.</p>" +
        "<p>Clique no botão abaixo para definir uma nova senha:</p>" +
        "<a href=\"" + resetLink + "\" style=\"padding:10px 20px; background:#007bff; color:white; text-decoration:none; border-radius:5px;\">Redefinir Senha</a>" +
        "<p>Se você não solicitou essa alteração, ignore este e-mail.</p>" +
        "<p>Atenciosamente,</p>" +
        "<p>Iury Ferreira Santana</p>" +
        "</body></html>";

      emailService.sendEmail(request.getEmail(), subject, htmlContent);

      if(from == null || from.isBlank()) {
        response.setData(Map.of(
          "token", resetToken,
          "link", resetLink
        ));
      }
    }

    response.setMessage("Email enviado com sucesso.");
    return ResponseEntity.ok(response);
  }

  public ResponseEntity<?> resetPassword(ResetPasswordRequest request) {
    CustomResponse<Void> response = new CustomResponse<>();

    Optional<User> existingUser = userRepository.findByPasswordResetToken(request.getToken());

    if(!existingUser.isPresent()) {
      response.setMessage("Não foi possível alterar senha, usuário não encontrado ou reset token inválido.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    User user = existingUser.get();
    String hashedPassword = passwordEncoder.encode(request.getPassword());
    user.setPassword(hashedPassword);
    user.setPasswordResetToken(null);
    userRepository.save(user);

    String token = jwtService.generateToken(user.getEmail());
    response.setMessage("Senha alterada com sucesso");

    return ResponseEntity.ok()
      .header("token", token)
      .body(response);
  }
}