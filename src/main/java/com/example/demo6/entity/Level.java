package com.example.demo6.entity;

import lombok.*;

@AllArgsConstructor
@Getter
public enum Level {
  // 처리할 때는 영어로, 사용자에게는 한글로 보이게
  NORMAL("고마운 분"), SILVER("귀한 분"), GOLD("천생연분");

  // enum 값에 한글화 파라미터를 추가하면 추가한 파라미터를 필드로 클래스에 추가함
  private final String name;

}
