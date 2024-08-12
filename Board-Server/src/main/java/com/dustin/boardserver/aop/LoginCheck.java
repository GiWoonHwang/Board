// 패키지 선언: 이 파일이 속한 패키지를 정의합니다.
package com.dustin.boardserver.aop;

// 필요한 라이브러리 임포트: 이 어노테이션이 사용할 수 있는 요소 타입과 유지 정책을 가져옵니다.
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @Retention 어노테이션: 이 어노테이션이 얼마나 오래 유지될지를 지정합니다.
// RetentionPolicy.RUNTIME: 런타임까지 이 어노테이션 정보가 유지되어야 한다는 것을 의미합니다.
@Retention(RetentionPolicy.RUNTIME)

// @Target 어노테이션: 이 어노테이션이 적용될 수 있는 요소의 타입을 정의합니다.
// ElementType.METHOD: 이 어노테이션은 메서드에만 적용될 수 있음을 의미합니다.
@Target(ElementType.METHOD)

// LoginCheck 어노테이션 정의: 이 어노테이션을 사용하여 메서드에서 로그인 체크를 수행할 수 있습니다.
public @interface LoginCheck {

    // UserType 열거형 정의: 이 열거형은 두 가지 유형의 사용자를 정의합니다.
    // USER: 일반 사용자
    // ADMIN: 관리자 사용자
    public static enum UserType {
        USER, ADMIN
    }

    // type() 메서드: 이 메서드는 어노테이션을 적용할 때 사용자가 지정할 수 있는 값을 정의합니다.
    // LoginCheck 어노테이션을 사용할 때, type 요소를 통해 USER 또는 ADMIN 중 하나를 선택하게 됩니다.
    UserType type();
}
