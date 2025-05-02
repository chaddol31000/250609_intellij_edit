package com.example.demo6.advice;

import com.example.demo6.exception.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.*;
import org.springframework.web.servlet.resource.*;

@RestControllerAdvice
public class RequestFaultAdvice {

  // 400 처리
  // ?pno=100 과 같이 필수 피라미터가 있지만 생략된 경우 (/post)
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<String> missingServletRequestParameterException(MissingServletRequestParameterException e ) {
    return ResponseEntity.status(400).body(e.getMessage());
  }

  // 또 다른 400 처리 (잘못된 파라미터 입력)
  // int 가 필요한데 /post?pno=aaa 처럼 문자열이 온 경우
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<String> methodArgumentTypeMismatchException (MethodArgumentTypeMismatchException e) {
    return ResponseEntity.status(400).body(e.getMessage());
  }

  // 401 : 인증이 필요하다 → 스프링 시큐리티

  // 403 : 인가(권한확인) 이 필요하다 → 스프링 시큐리티

  // 404 처리
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<String> handleState404(NoResourceFoundException e) {
    // No static resource posts/post.(현재 오류 메시지) → "No", "static", "resource" ....
    // 문자열 분리하기. 하면 ↗ 처럼 나옴
    String[] messages = e.getMessage().split(" ");
    // 분리된 문자열 배열의 마지막원소를 꺼낸다 : "posts/post"
    String url = messages[messages.length-1];
    // 점을 제거하자
    url = url.substring(0, url.length()-1);

    System.out.println(messages[messages.length-1]);
    return ResponseEntity.status(404).body("잘못된 주소입니다 : " + url);
  }

  // 405 : 잘못된 메소드
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<String> handleState405() {
    return ResponseEntity.status(405).body("잘못된 메소드");
  }
}
