package com.example.demo6.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class AuthController {
  // 프론트엔드에서 현재 로그인 상태를 물어오면 응답
  // 로그인한 경우 : 200 + 로그인 아이디
  // 비로그인 : 409 + null
  // 409 는 우리가 원하지 않은 결과가 나와서 그렇게 나오게 만들었음
  // 하지만 409 말고 다른 에러 메시지가 출력된다면 백으로 돌아와 콘솔창을 봐야함

  @GetMapping(path="/api/auth/check")
  public ResponseEntity<Map<String, String>> checkLogin(Principal principal, HttpSession session) {
    if(principal!=null)
      return ResponseEntity.ok(Map.of("username", principal.getName()));
    return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
  }
}
