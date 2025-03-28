package com.waddahex.financeiroapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.waddahex.financeiroapi.dto.*;
import com.waddahex.financeiroapi.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
  @Autowired
  private AuthService authService;

  @PostMapping("/signin")
  public ResponseEntity<?> signin(@Valid @RequestBody SigninupRequest loginRequest) {
    return authService.signin(loginRequest);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@Valid @RequestBody SigninupRequest signupRequest) {
    return authService.signup(signupRequest);
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    return authService.forgotPassword(request);
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    return authService.resetPassword(request);
  }
}