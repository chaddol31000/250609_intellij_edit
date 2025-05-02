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

    // 2. 프사를 업로드 했다면 저장을 위해 base64 인코딩, 프사 확인 (업로드하지 않았다면 기본 프사를 저장)
    MultipartFile profile = dto.getProfile();
    // 프론트에 <input type='file' name='profile'> 이 없다면 profile 이 null 이 된다
    // 이 경우 profile.isEmpty() 는 null pointer exception(NPE)
    boolean 프사_존재 = profile!=null && !profile.isEmpty();
    // <input type='file' name='profile'> 이렇게 input 이 있지만 선택은 하지 않음 → null은 아님
    // <input type='text' name='username'> → 입력을 하지 않았다 → 서버에서 꺼내면 null 이 아니라 ""
    String base64Image = "";
    try {
      if(프사_존재) {
        // 사용자가 업로드한 이미지를 base64 로 바꾸는 함수는 실패할 수 있다
        base64Image = Demo6Util.convertToBase64(profile);
      } else {
        base64Image = Demo6Util.getDefaultBase64Profile();
      }
    } catch(IOException e) {

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

  public MemberDto.Read read(String loginId) {
    Member member = memberDao.findByUsername(loginId);
    return member.toRead();
  }

  public boolean changePassword(MemberDto.PasswordChange dto, String loginId) {
    // 기존 암호화된 비밀번호를 읽어와 비밀번호가 맞는 지 확인하는 과정이 필요함
    // 비밀번호가 일치한 경우 새 비밀번호로 업데이트 → 틀리면 false
    String encodedPassword = memberDao.findPasswordByUsername(loginId);
    if(encoder.matches(dto.getCurrentPassword(), encodedPassword))
      return false;
    // 비밀번호가 일치한 경우 새 비밀번호로 업데이트
    return memberDao.updatePassword(loginId, encoder.encode(dto.getNewPassword()))==1;
  }

  public void resign(String loginId) {
    memberDao.delete(loginId);
  }
}
