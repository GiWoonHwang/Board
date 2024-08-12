package com.dustin.boardserver.service.impl;

// 필요한 패키지와 클래스들을 임포트합니다.
import com.dustin.boardserver.dto.UserDTO;
import com.dustin.boardserver.exception.DuplicateIdException;
import com.dustin.boardserver.mapper.UserProfileMapper;
import com.dustin.boardserver.service.UserService;
import com.dustin.boardserver.utils.SHA256Util;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

// 이 클래스는 UserService 인터페이스의 구현체로, 사용자와 관련된 비즈니스 로직을 처리합니다.
@Service
@Log4j2 // 로그를 사용하기 위해 Lombok의 @Log4j2 어노테이션을 사용합니다.
public class UserServiceImpl implements UserService {

    // UserProfileMapper 객체를 자동으로 주입받아 사용하기 위한 필드를 선언합니다.
    @Autowired
    private UserProfileMapper userProfileMapper;

    // 생성자에서 UserProfileMapper를 주입받아 초기화합니다.
    public UserServiceImpl(UserProfileMapper userProfileMapper) {
        this.userProfileMapper = userProfileMapper;
    }

    // 사용자의 정보를 조회하는 메서드입니다.
    @Override
    public UserDTO getUserInfo(String userId) {
        return userProfileMapper.getUserProfile(userId); // 사용자 ID로 프로필을 조회하여 반환합니다.
    }

    // 새로운 사용자를 등록하는 메서드입니다.
    @Override
    public void register(UserDTO userDTO) {
        // 중복된 아이디인지 확인합니다.
        boolean duplIdResult = isDuplicatedId(userDTO.getUserId());
        if (duplIdResult) {
            // 중복된 아이디일 경우 예외를 던집니다.
            throw new DuplicateIdException("중복된 아이디입니다.");
        }
        // 사용자 생성 시간 설정
        userDTO.setCreateTime(new Date());
        // 비밀번호를 SHA-256으로 암호화합니다.
        userDTO.setPassword(SHA256Util.encryptSHA256(userDTO.getPassword()));
        // 사용자 등록을 시도합니다.
        int insertCount = userProfileMapper.register(userDTO);

        // 등록에 실패했을 경우 로그를 남기고 예외를 던집니다.
        if (insertCount != 1) {
            log.error("insertMember ERROR! {}", userDTO);
            throw new RuntimeException(
                    "insertUser ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + userDTO);
        }
    }

    // 사용자의 로그인 정보를 확인하는 메서드입니다.
    @Override
    public UserDTO login(String id, String password) {
        // 입력된 비밀번호를 SHA-256으로 암호화합니다.
        String cryptoPassword = SHA256Util.encryptSHA256(password);
        // 아이디와 암호화된 비밀번호로 사용자 정보를 조회합니다.
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, cryptoPassword);
        return memberInfo; // 조회된 사용자 정보를 반환합니다.
    }

    // 아이디가 중복되었는지 확인하는 메서드입니다.
    @Override
    public boolean isDuplicatedId(String id) {
        // 아이디가 이미 존재하는지 여부를 반환합니다.
        return userProfileMapper.idCheck(id) == 1;
    }

    // 사용자의 비밀번호를 변경하는 메서드입니다.
    @Override
    public void updatePassword(String id, String beforePassword, String afterPassword) {
        // 현재 비밀번호를 SHA-256으로 암호화합니다.
        String cryptoPassword = SHA256Util.encryptSHA256(beforePassword);
        // 아이디와 암호화된 현재 비밀번호로 사용자 정보를 조회합니다.
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, cryptoPassword);

        // 사용자 정보가 존재할 경우 비밀번호를 업데이트합니다.
        if (memberInfo != null) {
            memberInfo.setPassword(SHA256Util.encryptSHA256(afterPassword)); // 새 비밀번호를 암호화하여 설정합니다.
            int insertCount = userProfileMapper.updatePassword(memberInfo); // 비밀번호를 업데이트합니다.
        } else { // 사용자 정보가 존재하지 않을 경우 로그를 남기고 예외를 던집니다.
            log.error("updatePassword ERROR! {}", memberInfo);
            throw new IllegalArgumentException("updatePassword ERROR! 비밀번호 변경 메서드를 확인해주세요\n" + "Params : " + memberInfo);
        }
    }

    // 사용자를 삭제하는 메서드입니다.
    @Override
    public void deleteId(String id, String passWord) {
        // 입력된 비밀번호를 SHA-256으로 암호화합니다.
        String cryptoPassword = SHA256Util.encryptSHA256(passWord);
        // 아이디와 암호화된 비밀번호로 사용자 정보를 조회합니다.
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, cryptoPassword);

        // 사용자 정보가 존재할 경우 사용자 프로필을 삭제합니다.
        if (memberInfo != null) {
            userProfileMapper.deleteUserProfile(memberInfo.getUserId());
        } else { // 사용자 정보가 존재하지 않을 경우 로그를 남기고 예외를 던집니다.
            log.error("deleteId ERROR! {}", memberInfo);
            throw new RuntimeException("deleteId ERROR! id 삭제 메서드를 확인해주세요\n" + "Params : " + memberInfo);
        }
    }

}
