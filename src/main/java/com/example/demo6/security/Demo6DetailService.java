package com.example.demo6.security;

// 아이디를 가지고 db 에 사용자 정보를 읽어와 스프링 시큐리티에 넘겨주는 클래스

import com.example.demo6.dao.*;
import com.example.demo6.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

@Component
// implements : 표준
public class Demo6DetailService implements UserDetailsService {
  @Autowired
  private MemberDao memberDao;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member m = memberDao.loadLoginData(username).orElseThrow(()-> new UsernameNotFoundException("사용자가 없습니다"));

    // 아이디, 비밀번호, 권한, 계정 블록 여부 등을 담은 스프링 시큐리티 표준 UserDetails 를 리턴
    //                                                                        ↓  enum 을 문자열로 바꾸면 name
    return User.builder().username(m.getUsername()).password(m.getPassword()).roles(m.getRole().name())
        .accountLocked(m.isLock()).build();
  }
}
