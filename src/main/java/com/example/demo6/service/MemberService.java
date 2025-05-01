package com.example.demo6.service;

import com.example.demo6.dao.*;
import com.example.demo6.dto.*;
import com.example.demo6.entity.*;
import com.example.demo6.util.*;
import jakarta.validation.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

import java.io.*;
import java.util.*;

@Service
public class MemberService {
  @Autowired
  private MemberDao memberDao;
  @Autowired
  private PasswordEncoder encoder;

  public boolean checkUsername(MemberDto.UsernameCheck dto) {
    return !memberDao.existsByUsername(dto.getUsername());
  }

  public Member signup(MemberDto.Create dto) {
    // 컨트롤러 : DTO 로 작업 (DTO 는 화면을 따라 간다)
    // 데이터베이스는 가급적 entity 로 작업 (Entity 는 db 를 따라간다)
    // 개발자는 가운데에서 번역해주는 역할을 한다고 생각하면 됨 (Entity 와 Dto 사이의 Service 역할)

    // 비밀번호 암호화했다고 치자
//    String encodedPassword = dto.getPassword();
//    Member member = dto.toEntity(encodedPassword);
//    memberDao.save(member);
//    return member;

    // 1. 비밀번호 암호화

    String encodedPassword = encoder.encode(dto.getPassword());
    // 2. 프사를 업로드 했다면 저장을 위해 base64 인코딩
    MultipartFile profile = dto.getProfile();
    // <input type='file' name='profile'> 이렇게 input 이 있지만 선택은 하지 않음 → null은 아님
    // <input type='text' name='username'> → 입력을 하지 않았다 → 서버에서 꺼내면 null 이 아니라 ""
    String base64Image = "";
    if(!profile.isEmpty()) {
      try {
        base64Image = Demo6Util.convertToBase64(profile);
      } catch(IOException e) {
        e.printStackTrace();
      }
    }
    // 3. 암호화된 비밀번호, base64 이미지를 가지고 dto 를 member 로 변환
    Member member = dto.toEntity(encodedPassword, base64Image);
    memberDao.save(member);
    return member;
  }

  public Optional<String> searchUsername(String email) {
    return memberDao.findUsernameByEmail(email);
  }

  public Optional<String> getTemporaryPassword(MemberDto.GeneratePassword dto) {
    // 1. 아이디와 이메일이 일치하는 사용자가 있는 지 확인
    // 2. 사용자가 없을 경우 비어있는 Optional 을 리턴 → 컨트롤러에서 if 문으로 처리
    // 3. 있다면 임시 비밀번호 생성
    // 4. 임시 비밀번호를 암호화해서 사용자 정보를 업데이트
    // 5. 비밀번호를 Optional 로 리턴
    boolean isExist = memberDao.existsByUsernameAndEmail(dto);
    if(!isExist)
      return Optional.empty();
    String newPassword = RandomStringUtils.secure().nextAlphanumeric(20);
    memberDao.updatePassword(dto.getUsername(), newPassword);
    return Optional.ofNullable(newPassword);
  }
}
