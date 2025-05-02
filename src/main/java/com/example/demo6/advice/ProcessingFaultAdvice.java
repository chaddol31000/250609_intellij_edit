package com.example.demo6.advice;

import com.example.demo6.exception.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ProcessingFaultAdvice {
  // 500 : 처리 중 오류. 정말 다양한 이유로 발생

  // 검증 실패에 대한 예외 처리
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> constraintViolationException() {
    return ResponseEntity.status(409).body("잘못된 입력 형식");
  }

  // 사용자 정의 : 엔티티 클래스 (회원, 글, 댓글) 가 없을 때
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> entityNotFoundException(EntityNotFoundException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }
}
