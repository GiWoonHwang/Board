package com.dustin.boardserver.dto.response;

import com.dustin.boardserver.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

// @Getter: Lombok 어노테이션으로, 이 클래스의 모든 필드에 대해 getter 메서드를 자동으로 생성합니다.
@Getter

// @AllArgsConstructor: Lombok 어노테이션으로, 모든 필드를 매개변수로 받는 생성자를 자동으로 생성합니다.
@AllArgsConstructor

// @RequiredArgsConstructor: Lombok 어노테이션으로, @NonNull이 붙은 필드에 대해 생성자를 자동으로 생성합니다.
// 이 생성자는 필수적인 필드만 초기화할 수 있게 합니다.
@RequiredArgsConstructor
public class LoginResponse {

    // 로그인 상태를 나타내는 열거형(enum)입니다.
    // SUCCESS: 로그인 성공
    // FAIL: 로그인 실패
    // DELETED: 계정 삭제됨
    enum LoginStatus {
        SUCCESS, FAIL, DELETED
    }

    // 로그인 결과를 나타내는 필드입니다. 필수적으로 설정되어야 하는 필드이며, null이 될 수 없습니다.
    @NonNull
    private LoginStatus result;

    // 로그인한 사용자의 정보를 담는 객체입니다. 로그인 성공 시에만 값이 설정됩니다.
    private UserDTO userDTO;

    // 실패한 로그인 응답을 나타내는 상수입니다. LoginStatus.FAIL 상태로 설정됩니다.
    private static final LoginResponse FAIL = new LoginResponse(LoginStatus.FAIL);

    // 로그인 성공 시 호출되는 정적 메서드입니다.
    // 성공적인 로그인 결과와 함께 UserDTO 객체를 반환합니다.
    public static LoginResponse success(UserDTO userDTO) {
        return new LoginResponse(LoginStatus.SUCCESS, userDTO);
    }
}
