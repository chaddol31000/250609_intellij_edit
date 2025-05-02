package com.example.demo6;

import com.example.demo6.dao.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.security.crypto.password.*;

@SpringBootTest
public class MemberDaoTest {
  @Autowired
  private MemberDao memberDao;
  @Autowired
  private PasswordEncoder encoder;

  //@Test
  public void test() {
    System.out.println(memberDao.loadLoginData("summer"));
  }

  @Test
  public void 비밀번호확인() {
      // 사용자로부터 입력받는 클래스, MyBatis 출력하는 클래스는 반드시 setter 가 있어야 한다
        // ㄴ 스프링빈, 마이바티스 리턴 객체는 기본생성자가 있어야 한다
        // ㄴ 기본 생성자로 객체를 생성한 다음 setter 로 값을 집어 넣는다
        // ㄴ @AllArgsConstructor 를 사용하지 않는다
        // ㄴ @Data - @Getter, @Setter, @ToString 이 세 역할을 한 번에 하는 스프링 어노테이션
    String username = "summer";
    String password = memberDao.loadLoginData(username).get().getPassword();
    System.out.println(encoder.matches("123456",password));
  }
}
