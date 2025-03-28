package com.waddahex.financeiroapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.waddahex.financeiroapi.dto.CustomResponse;
import com.waddahex.financeiroapi.dto.ForgotPasswordRequest;
import com.waddahex.financeiroapi.dto.ResetPasswordRequest;
import com.waddahex.financeiroapi.dto.SigninupRequest;
import com.waddahex.financeiroapi.model.User;
import com.waddahex.financeiroapi.repository.UserRepository;
import com.waddahex.financeiroapi.security.JwtService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AuthServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private JwtService jwtService;

  @Mock
  private EmailService emailService;

  @InjectMocks
  private AuthService authService;

  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    passwordEncoder = new BCryptPasswordEncoder();
  }

  @Test
  void signin_ShouldReturnTokenWhenSuccess() {
    // Given
    String email = "test@example.com";
    String password = "password123";
    String hashedPassword = passwordEncoder.encode(password);
    User user = new User();
    user.setEmail(email);
    user.setPassword(hashedPassword);

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(jwtService.generateToken(email)).thenReturn("mockedToken");

    SigninupRequest request = new SigninupRequest();
    request.setEmail(email);
    request.setPassword(password);

    // When
    ResponseEntity<?> response = authService.signin(request);

    // Then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getHeaders().get("token"));
    assertEquals("Usuário logado com sucesso!", ((CustomResponse<?>) response.getBody()).getMessage());
  }

  @Test
  void signin_ShouldNotReturnTokenWhenInvalidPassword() {
    // Given
    String email = "test@example.com";
    String wrongPassword = "wrongPassword";
    String correctPassword = "password123";
    String hashedPassword = passwordEncoder.encode(correctPassword);
    User user = new User();
    user.setEmail(email);
    user.setPassword(hashedPassword);

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    SigninupRequest request = new SigninupRequest();
    request.setEmail(email);
    request.setPassword(wrongPassword);

    // When
    ResponseEntity<?> response = authService.signin(request);

    // Then
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("Email ou senha inválidos!", ((CustomResponse<?>) response.getBody()).getMessage());
  }

  @Test
  void signin_ShouldNotReturnTokenWhenUserNotFound() {
    // Given
    String email = "notfound@example.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    SigninupRequest request = new SigninupRequest();
    request.setEmail(email);
    request.setPassword("password");

    // When
    ResponseEntity<?> response = authService.signin(request);

    // Then
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("Email ou senha inválidos!", ((CustomResponse<?>) response.getBody()).getMessage());
  }

  @Test
  void signup_ShouldReturnTokenWhenSuccess() {
    // Given
    String email = "newuser@example.com";
    String password = "password123";

    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
    when(jwtService.generateToken(email)).thenReturn("mockedToken");

    User newUser = new User();
    newUser.setEmail(email);
    newUser.setPassword(passwordEncoder.encode(password));

    when(userRepository.save(any(User.class))).thenReturn(newUser);

    SigninupRequest request = new SigninupRequest();
    request.setEmail(email);
    request.setPassword(password);

    // When
    ResponseEntity<?> response = authService.signup(request);

    // Then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getHeaders().get("token"));
    assertEquals("Usuário criado com sucesso!", ((CustomResponse<?>) response.getBody()).getMessage());
  }

  @Test
  void signup_ShouldNotReturnTokenWhenUserAlreadyExists() {
    // Given
    String email = "existing@example.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

    SigninupRequest request = new SigninupRequest();
    request.setEmail(email);
    request.setPassword("password123");

    // When
    ResponseEntity<?> response = authService.signup(request);

    // Then
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertEquals("Já existe usuário com esse email!", ((CustomResponse<?>) response.getBody()).getMessage());
  }

  @Test
  void forgotPassword_ShouldSendEmailWhenUserExists() {
    // Given
    String email = "test@example.com";
    User user = new User();
    user.setEmail(email);

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    ForgotPasswordRequest request = new ForgotPasswordRequest();
    request.setEmail(email);

    // When
    ResponseEntity<?> response = authService.forgotPassword(request);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Email enviado com sucesso.", ((CustomResponse<?>) response.getBody()).getMessage());
    verify(emailService, times(1)).sendEmail(eq(email), anyString(), anyString());
  }

  @Test
  void forgotPassword_ShouldNotSendEmailWhenUserDontExist() {
    // Given
    String email = "notfound@example.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    ForgotPasswordRequest request = new ForgotPasswordRequest();
    request.setEmail(email);

    // When
    ResponseEntity<?> response = authService.forgotPassword(request);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Email enviado com sucesso.", ((CustomResponse<?>) response.getBody()).getMessage());
    verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
  }

  @Test
  void resetPassword_ShouldReturnTokenWhenSuccess() {
    // Given
    String token = "valid-token";
    String newPassword = "newPassword123";

    User user = new User();
    user.setEmail("test@example.com");
    user.setPassword("oldPassword");
    user.setPasswordResetToken(token);

    ResetPasswordRequest request = new ResetPasswordRequest();
    request.setToken(token);
    request.setPassword(newPassword);

    when(userRepository.findByPasswordResetToken(token)).thenReturn(Optional.of(user));
    when(jwtService.generateToken(user.getEmail())).thenReturn("mockedToken");
    when(userRepository.save(user)).thenReturn(user);

    // When
    ResponseEntity<?> response = authService.resetPassword(request);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Senha alterada com sucesso", ((CustomResponse<?>) response.getBody()).getMessage());
    assertEquals("mockedToken", response.getHeaders().getFirst("token"));
    verify(userRepository).save(user);
  }

  @Test
  void resetPassword_ShouldNotReturnTokenWhenUserNotFound() {
    // Given
    String token = "invalid-token";
    ResetPasswordRequest request = new ResetPasswordRequest();
    request.setToken(token);
    request.setPassword("newPassword123");

    when(userRepository.findByPasswordResetToken(token)).thenReturn(Optional.empty());

    // When
    ResponseEntity<?> response = authService.resetPassword(request);

    // Then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Não foi possível alterar senha, usuário não encontrado ou reset token inválido.", ((CustomResponse<?>) response.getBody()).getMessage());
    verify(userRepository, never()).save(any(User.class));
  }
}