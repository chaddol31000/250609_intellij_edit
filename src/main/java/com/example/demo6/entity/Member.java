package com.example.demo6.entity;

import com.example.demo6.dto.*;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.web.multipart.*;

import java.time.*;
import java.time.temporal.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
  // 사용자에게 받아올 값
  private String username;
  // 아예 출력되지 않음 (진짜. 네버. 절대)
  @JsonIgnore
  private String password;
  private String email;
  // 프로필 사진 이름
  // MultipartFile : 파일 업로드하는 객체.
  // File : 하드에 저장되어 있는 파일
  // blob : 용량이 적다는 장점이 있지만, 사용자에게 보여줄 때는 clob 로 다시 변환해서 보내야 함
  // MultiFile : 메모리에 떠있는 파일 (PC 껐다 키면 사라짐)
  private String profile;

  // 초기화 할 수 있는 값
  // builder 가 들어가면 값이 초기화가 되지 않음 그래서 추가 어노테이션이 필요함

  @Builder.Default
  private LocalDate joinDay = LocalDate.now();
  // 역할 : USER, MANAGER, ADMIN
  @Builder.Default
  private Role role = Role.USER;
  // 레벨
  @Builder.Default
  private Level level = Level.NORMAL;
  // 로그인 관련 데이터들 : 로그인 실패횟수, 계정 블록 여부
  @Builder.Default
  private int failedAttempts = 0;
  @Builder.Default
  private boolean isLock = false;

  public MemberDto.Read toRead() {
    long days = ChronoUnit.DAYS.between(joinDay, LocalDate.now());
    return new MemberDto.Read(username, email, profile, joinDay, days, level);
  }
}
