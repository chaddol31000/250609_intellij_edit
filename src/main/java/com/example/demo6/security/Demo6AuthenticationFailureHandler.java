package com.example.demo6.security;

import com.example.demo6.dao.*;
import com.example.demo6.entity.*;
import com.example.demo6.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;

import java.io.*;

// 로그인에 실패하면 "로그인에 몇 회 실패했습니다" 라고 출력
// 로그인에 5번 실패하면 계정을 블록한 다음("계정이 블록되었습니다" 라고 출력)

@Component
public class Demo6AuthenticationFailureHandler implements AuthenticationFailureHandler {
  @Autowired
  private MemberDao memberDao;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    // 로그인에 실패하는 이유
      // 1. 계정이 블록되어 실패 (403 / 권한이 필요하다 / 아이디와 비밀번호는 맞고 권한이 없는 것)
      // 2. 아이디나 비밀번호가 틀려서 실패 (401 / 로그인(인증) 이 필요하다/ 로그인 자체가 실패된 것)

    if (exception instanceof LockedException) {
      ResponseUtil.sendJsonResponse(response, 403, "블록 된 계정입니다");
    } else {
      ResponseUtil.sendJsonResponse(response, 401, "로그인 실패했습니다");
    }


  }
}

