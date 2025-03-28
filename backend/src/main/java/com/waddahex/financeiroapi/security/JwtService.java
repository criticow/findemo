package com.waddahex.financeiroapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
  @Value("${jwt.secret}")
  private String secretKey;

  public String generateToken(String email) {
    Algorithm algorithm = Algorithm.HMAC256(secretKey);

    return JWT.create()
              .withSubject(email)
              .withIssuedAt(new Date())
              .sign(algorithm);
  }

  public String validateToken(String token) {
    try {
      DecodedJWT decodedJWT = JWT.decode(token);
      return decodedJWT.getSubject();
    }catch(Exception e){
      return null;
    }
  }
}


