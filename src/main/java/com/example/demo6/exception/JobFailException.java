package com.example.demo6.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 자바의 예외
// Exception 의 자식들 : try ~ catch 예외처리 강제 (체크하는 예외)
//  ↖ RuntimeException 의 자식들 : 예외처리 선택 (체크하지 않는 예외)
// 스프링 예외처리는 보통 체크하지 않는 예외를 사용한다 → RuntimeException 은 직접 사용하기에는 너무 광범위함
@AllArgsConstructor
@Getter
public class JobFailException extends RuntimeException {
  private String message;
}
