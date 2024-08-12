package com.dustin.boardserver.dto;

import lombok.*;

// @Getter: Lombok 어노테이션으로, 이 클래스의 모든 필드에 대해 getter 메서드를 자동으로 생성합니다.
// 이를 통해 필드 값을 외부에서 읽을 수 있습니다.
@Getter

// @Setter: Lombok 어노테이션으로, 이 클래스의 모든 필드에 대해 setter 메서드를 자동으로 생성합니다.
// 이를 통해 필드 값을 외부에서 설정할 수 있습니다.
@Setter

// @ToString: Lombok 어노테이션으로, 이 클래스의 모든 필드를 포함한 toString 메서드를 자동으로 생성합니다.
// 이를 통해 객체의 필드값들을 문자열로 반환할 수 있습니다.
@ToString

// @AllArgsConstructor: Lombok 어노테이션으로, 모든 필드를 매개변수로 받는 생성자를 자동으로 생성합니다.
// 이를 통해 객체를 생성할 때 모든 필드를 초기화할 수 있습니다.
@AllArgsConstructor

// @NoArgsConstructor: Lombok 어노테이션으로, 매개변수가 없는 기본 생성자를 자동으로 생성합니다.
// 이를 통해 필드를 초기화하지 않고 빈 객체를 생성할 수 있습니다.
@NoArgsConstructor
public class CategoryDTO {

    // 카테고리의 고유 ID를 나타내는 필드입니다.
    private int id;

    // 카테고리의 이름을 나타내는 필드입니다.
    private String name;

    // 카테고리의 정렬 상태를 나타내는 필드입니다.
    // 예를 들어, 최신순, 인기순 등의 정렬 상태를 정의할 수 있습니다.
    private SortStatus sortStatus;

    // 이 카테고리가 검색된 횟수를 나타내는 필드입니다.
    private int searchCount;

    // 페이지네이션을 위한 시작 오프셋을 나타내는 필드입니다.
    // 예를 들어, 검색 결과를 페이지로 나눌 때 어디서부터 시작할지를 정의합니다.
    private int pagingStartOffset;

}
