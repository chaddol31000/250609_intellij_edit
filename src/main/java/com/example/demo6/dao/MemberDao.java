package com.example.demo6.dao;

import com.example.demo6.dto.*;
import com.example.demo6.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.*;

@Mapper
public interface MemberDao {
  @Select("select count(*) from members where username=#{username}")
  boolean existsByUsername(String username);

  int save(Member member);

  // 체크 코드가 활성화 되었는지 is_lock 의 boolean 으로 확인
  @Update("update members set is_lock=0 where code=#{code} and rownum=1")
  int verifyCode(String code);

  @Select("select username from members where email=#{email} and rownum=1")
  Optional<String> findUsernameByEmail(String email);

  @Update("update members set password=#{newPassword} where username=#{username}")
  int updatePassword(String username, String newPassword);

  @Select("select username, password, role, is_lock from members where username=#{username}")
  Optional<Member> loadLoginData(String username);

  @Select("select failed_attempts from members where username=#{username}")
  Optional<Integer> 로그인실패횟수 (String name);

  @Update("update members set failed_attempts=failed_attempts+1 where username=#{username}")
  int 로그인실패횟수증가(String 로그인_시도_아이디);

  @Update("update members set is_Lock=1 where username=#{username}")
  int 계정블록(String 로그인_시도_아이디);

  @Update("update members set failed_attempts=0 where username=#{username}")
  int reset로그인실패횟수(String loginId);

  @Select("select username, email, profile, join_day, levels from members where username=#{loginId}")
  Member findByUsername(String loginId);

  @Select("select password from members where username=#{loginId}")
  String findPasswordByUsername(String loginId);

  @Delete("delete from members where username=#{loginId}")
  int delete(String loginId);
}
