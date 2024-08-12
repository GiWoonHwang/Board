package com.dustin.boardserver.dto.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

// @Setter: Lombok 어노테이션으로, 이 클래스의 모든 필드에 대해 setter 메서드를 자동으로 생성합니다.
@Setter

// @Getter: Lombok 어노테이션으로, 이 클래스의 모든 필드에 대해 getter 메서드를 자동으로 생성합니다.
@Getter
public class UserUpdatePasswordRequest {

    // @NonNull: Lombok 어노테이션으로, 이 필드가 null이 아니어야 함을 명시합니다.
    // 이 어노테이션을 사용하면 null 값이 설정될 경우, NullPointerException이 발생합니다.
    @NonNull
    private String beforePassword; // 비밀번호 변경 전에 사용하던 기존 비밀번호를 나타냅니다.

    @NonNull
    private String afterPassword; // 새로운 비밀번호를 나타냅니다.
}