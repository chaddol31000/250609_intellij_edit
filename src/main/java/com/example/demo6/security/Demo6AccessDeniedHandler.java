package com.example.demo6.security;

import com.example.demo6.util.*;
import com.fasterxml.jackson.databind.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.access.*;
import org.springframework.security.web.access.*;
import org.springframework.stereotype.*;

import java.io.*;

// 403(권한 없음) 을 처리하는 자바코드
// 403 이 발생하면 원래 html 로 오류 화면이 출력됨 (rest 는 html 로 응답하면 안됨) → ResponseEntity 로 바꾸자
// → 여기는 리턴이 void 인데? (ResponseEntity 로 리턴이 불가능) →
@Component
public class Demo6AccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    // 스프링은 json 출력 기능이 없음 → 외부 라이브러리를 사용
    // 스프링의 기본 json 라이브러리는 jackson 라이브러리
    // rest 방식은 데이터 + 상태코드로 응담 → 스프링이라면 ResponseEntity 를 리턴하면 알아서 json 으로 바꿔서 출력
    // 스프링 시큐리티의 핸들러들은 사실 자바코드 → 내가 수동으로 json 변환을 해야 한다
    // 스프링의 구조라면 return ResponseEntity.status(403).body("작업 권한이 없습니다")
//    ObjectMapper mapper = new ObjectMapper();
//    response.setStatus(403);
//
//    // response 로 응답하면 한글 출력에 문제가 있다 (한글이 깨진다)
//    response.setContentType("application/json; charset=UTF-8");
//    response.getWriter().write(mapper.writeValueAsString("작업 권한이 없습니다"));
    ResponseUtil.sendJsonResponse(response, 403, "작업 권한이 없습니다");
  }
}
