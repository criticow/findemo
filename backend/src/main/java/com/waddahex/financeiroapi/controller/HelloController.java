package com.waddahex.financeiroapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.waddahex.financeiroapi.dto.CustomResponse;

@RestController
@RequestMapping("/")
public class HelloController {
  @GetMapping
  public ResponseEntity<?> hello() {
    CustomResponse<Void> response = new CustomResponse<>();

    response.setMessage("Sevidor online!");

    return ResponseEntity.ok(response);
  }
}
