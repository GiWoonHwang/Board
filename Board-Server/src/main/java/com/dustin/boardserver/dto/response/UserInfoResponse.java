package com.dustin.boardserver.dto.response;

import com.dustin.boardserver.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

// @Getter: Lombok 어노테이션으로, 이 클래스의 모든 필드에 대해 getter 메서드를 자동으로 생성합니다.
// 이를 통해 필드 값을 외부에서 읽을 수 있습니다.
@Getter

// @AllArgsConstructor: Lombok 어노테이션으로, 모든 필드를 매개변수로 받는 생성자를 자동으로 생성합니다.
// 이를 통해 객체를 생성할 때 필드를 초기화할 수 있습니다.
@AllArgsConstructor
public class UserInfoResponse {

    // 로그인한 사용자에 대한 정보를 담고 있는 UserDTO 객체입니다.
    private UserDTO userDTO;
}
