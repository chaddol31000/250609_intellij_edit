package com.example.demo6.dto;

import com.example.demo6.entity.*;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.*;

import java.time.*;

public class MemberDto {

  @Data
  public static class UsernameCheck {
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9]{6,10}$")
    private String username;
  }
  @Data
  public static class Create {
    @NotEmpty
    // "정규식" : 문자열 패턴을 검증할 때 사용하는 기술 (ex. 아이디는 소문자와 숫자 6~10자)
    // [a-z] : 소문자 1글자
    // [0-9] : 숫자 1개
    // [a-z0-9] : 소문자 또는 숫자 1글자 (범위들을 나열하면 '-또는' 으로 연결
    // [a-z0-9]{6-10} : 소문자 또는 숫자 6~10자 (을 포함하는)
                    //  현재 패턴에 "1111111111111111" 을 주면 통과
    // 아이디는 소문자와 숫자가 6글자 이상, 10글자를 초과하면 안된다
    // ^[a-z0-9]{6,10}$
      // ^ : 시작한다는 뜻
      // $ : 끝난다는 뜻
    @Pattern(regexp = "^[a-z0-9]{6,10}$")
    private String username;
    @NotEmpty
    // 비밀번호 대문자, 소문자, 숫자 6~10글자
    @Pattern(regexp = "^[A-Za-z0-9]{6,10}$")
    private String password;
    @NotEmpty
    @Email
    private String email;
    private MultipartFile profile;
    // MultipartFile = 파일을 업로드 할 때 사용 (메모리에 떠 있다)
    // 메모리에 떠 있기 때문에 큰 파일이 메모리에 올라가게 되면 서버가 죽어버림
    // 그래서 용량 제한을 걸고 다시 검증해야함


    // DTO 를 엔티티로 변환하는 메소드
    // 프론트에서는 String, 백에서는 MultipartFile 로 변환
    public Member toEntity(String encodedPassword, String base64Image) {
      return Member.builder().username(username).password(encodedPassword).email(email).profile(base64Image).build();
    }
  }

  @Data
  public static class GeneratePassword {
    @NotEmpty
    @Pattern(regexp="^[a-z0-9]{6,10}$")
    private String username;
    @NotEmpty
    @Email
    private String email;
  }

  @Data
  @AllArgsConstructor
  public static class Read {
    private String username;
    private String email;
    private String profile;
    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDate joinDay;
    // 가입 기간
    private long days;
    private Level level;
  }

  @Data
  public static class PasswordChange {
    @NotEmpty
    @Pattern(regexp = "^[a-zA-z0-9]{6,10}$")
    private String currentPassword;
    @NotEmpty
    @Pattern(regexp = "^[a-zA-z0-9]{6,10}$")
    private String newPassword;
  }
}
