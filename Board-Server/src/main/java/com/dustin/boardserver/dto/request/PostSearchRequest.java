package com.dustin.boardserver.dto.request;

import com.dustin.boardserver.dto.SortStatus;
import lombok.*;

// @Builder: Lombok 어노테이션으로, 이 클래스에 대해 빌더 패턴을 적용할 수 있게 합니다.
// 빌더 패턴은 복잡한 객체를 생성할 때 가독성을 높이고, 유연한 객체 생성을 가능하게 합니다.
@Builder

// @Getter: Lombok 어노테이션으로, 이 클래스의 모든 필드에 대해 getter 메서드를 자동으로 생성합니다.
@Getter

// @Setter: Lombok 어노테이션으로, 이 클래스의 모든 필드에 대해 setter 메서드를 자동으로 생성합니다.
@Setter

// @ToString: Lombok 어노테이션으로, 이 클래스의 모든 필드를 포함하는 toString 메서드를 자동으로 생성합니다.
// 객체의 필드값들을 문자열로 반환하는 기능을 제공합니다.
@ToString

// @AllArgsConstructor: Lombok 어노테이션으로, 이 클래스의 모든 필드를 매개변수로 받는 생성자를 자동으로 생성합니다.
@AllArgsConstructor

// @NoArgsConstructor: Lombok 어노테이션으로, 매개변수가 없는 기본 생성자를 자동으로 생성합니다.
@NoArgsConstructor
public class PostSearchRequest {

    // 게시물의 ID를 나타내는 필드입니다.
    private int id;

    // 게시물의 이름을 나타내는 필드입니다.
    private String name;

    // 게시물의 내용을 나타내는 필드입니다.
    private String contents;

    // 게시물의 조회수를 나타내는 필드입니다.
    private int views;

    // 게시물이 속한 카테고리의 ID를 나타내는 필드입니다.
    private int categoryId;

    // 게시물을 작성한 사용자의 ID를 나타내는 필드입니다.
    private int userId;

    // 게시물을 정렬하는 기준을 나타내는 필드입니다.
    // 예를 들어, 최신순, 조회수순 등으로 정렬할 때 사용될 수 있습니다.
    private SortStatus sortStatus;
}
