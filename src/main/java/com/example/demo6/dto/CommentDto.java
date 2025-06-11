package com.example.demo6.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
  // private CommentDto() { } 와 같음
  // 왜 만드냐? 객체 생성을 금지하기 위해서
  // 두 개를 같이 만들 수 없음 의미가 중복되기 때문에
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDto {
  @Data
  public static class Create {
    @NotNull
    private Integer pno;
    @NotEmpty
    private String content;
  }

  @Data
  public static class Delete {
    @NotNull
    private Integer cno;
    // 댓글을 삭제하고 나머지 댓글들을 출력해야하기에 pno 를 불러와야함
    @NotNull
    private Integer pno;
  }
}
