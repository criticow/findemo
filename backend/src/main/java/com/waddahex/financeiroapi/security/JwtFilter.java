package com.waddahex.financeiroapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import com.waddahex.financeiroapi.model.User;
import com.waddahex.financeiroapi.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

@Configuration
public class JwtFilter extends OncePerRequestFilter {
  @Value("${jwt.secret}")
  private String secretKey;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private UserRepository userRepository;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    try {
      if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
        filterChain.doFilter(request, response);
        return;
      }

      String path = request.getServletPath();

      if(path.contains("auth") || path.equals("/"))
      {
        filterChain.doFilter(request, response);
        return;
      }

      String token = extractToken(request);
      String email = jwtService.validateToken(token);

      if(email == null) {
        filterChain.doFilter(request, response);
        return;
      }

      Optional<User> user = userRepository.findByEmail(email);

      if(user.isEmpty()) {
        filterChain.doFilter(request, response);
        return;
      }

      user.ifPresent(RequestContext::setUser);

      filterChain.doFilter(request, response);
    } finally {
      RequestContext.clear();
    }
  }

  private String extractToken(HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");

    if(authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.length() <= 7) return null;

    return authHeader.substring(7);
  }
}

