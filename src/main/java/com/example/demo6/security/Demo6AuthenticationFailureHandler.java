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
    // ob instanceof 사과 → true OR false

    // 1. 로그인 시도한 아이디를 가져와서
    // 2. db 에서 사용자 정보를 읽어온다
    // 3. 로그인 실패 횟수가 3번이하라면 로그인 실패 횟수 증가 → "로그인에 몇 회 실패했습니다"
    // 4. 로그인 실패 횟수가 4번이라면 5번으로 증가시키고 계정 → "로그인에 5회 실패해 계정이 블록되었습니다"


    // 계정이 이미 * 블록된 경우
    // " 블록된 계정입니다 "

    String message = "알 수 없는 이유로 로그인에 실패했습니다";

    // 아이디나 비밀번호가 틀린 경우
  if(exception instanceof BadCredentialsException) {
    String 로그인_시도_아이디 = request.getParameter("username");
    if(memberDao.로그인실패횟수(로그인_시도_아이디).isEmpty()) {
      // 아이디가 틀린 경우
      message = "아이디를 확인하세요";
    } else {
      // 비밀번호가 틀린 경우
      memberDao.로그인실패횟수증가(로그인_시도_아이디);
      int count = memberDao.로그인실패횟수(로그인_시도_아이디).get();
      // 비밀번호가 5회 이상 틀렸다면 계정 블록
      if(count>=5) {
        memberDao.계정블록(로그인_시도_아이디);
        message = "로그인에 5회 실패해 계정이 블록되었습니다";
      } else {
        // 비밀번호가 4회 이하로 틀린 경우
        message = "로그인에" + count + "회 실패했습니다";
      }
    }
  } else if (exception instanceof LockedException) {
    // 계정이 블록된 경우
    message = "로그인에 5회 이상 실패해 블록된 계정입니다";
  }
    ResponseUtil.sendJsonResponse(response, 401, message);

  }
}

