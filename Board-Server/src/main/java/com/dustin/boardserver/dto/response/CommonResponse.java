package com.dustin.boardserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

// @Getter: Lombok 어노테이션으로, 이 클래스의 모든 필드에 대해 getter 메서드를 자동으로 생성합니다.
@Getter

// @NoArgsConstructor: Lombok 어노테이션으로, 매개변수가 없는 기본 생성자를 자동으로 생성합니다.
@NoArgsConstructor

// @AllArgsConstructor: Lombok 어노테이션으로, 모든 필드를 매개변수로 받는 생성자를 자동으로 생성합니다.
@AllArgsConstructor
public class CommonResponse<T> {

    // HTTP 응답 상태를 나타내는 필드입니다. 예를 들어, 200 OK, 404 Not Found 등이 될 수 있습니다.
    private HttpStatus status;

    // 응답 코드입니다. 일반적으로 응답의 결과를 구분하기 위해 사용될 수 있습니다.
    private String code;

    // 응답 메시지입니다. 응답의 의미나 상세 정보를 설명하는 문자열입니다.
    private String message;

    // 요청의 결과로 반환되는 실제 데이터를 담고 있는 필드입니다.
    // 이 클래스는 제네릭 클래스로, T는 타입 매개변수로서 이 필드가 다양한 타입을 가질 수 있게 합니다.
    private T requestBody;
}