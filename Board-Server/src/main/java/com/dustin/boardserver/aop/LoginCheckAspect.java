package com.dustin.boardserver.aop;

import com.dustin.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


// @Component: 이 클래스가 Spring의 컴포넌트 스캔에 의해 자동으로 빈(bean)으로 등록되도록 합니다.
@Component

// @Aspect: 이 클래스가 AspectJ를 사용한 AOP(Aspect-Oriented Programming) 관점을 정의함을 나타냅니다.
@Aspect

// @Order: 이 클래스의 실행 순서를 지정합니다. Ordered.LOWEST_PRECEDENCE는 가장 낮은 우선순위를 의미합니다.
@Order(Ordered.LOWEST_PRECEDENCE)

// @Log4j2: 이 클래스에서 Log4j2를 사용하여 로그를 기록할 수 있게 합니다.
@Log4j2
public class LoginCheckAspect {

    // @Around: 특정 메서드 실행 전후에 이 메서드를 실행하도록 지정합니다.
    // "@annotation(com.dustin.boardserver.aop.LoginCheck) && @annotation(loginCheck)"
    // 이 부분은 LoginCheck 어노테이션이 적용된 메서드를 대상으로 하며, loginCheck 변수로 해당 어노테이션 인스턴스를 참조합니다.
    @Around("@annotation(com.dustin.boardserver.aop.LoginCheck) && @annotation(loginCheck)")
    public Object adminLoginCheck(ProceedingJoinPoint proceedingJoinPoint, LoginCheck loginCheck) throws Throwable {

        // HttpSession 객체를 얻어오기 위해 현재 요청의 속성을 가져옵니다.
        HttpSession session = (HttpSession) ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes()))
                .getRequest().getSession();

        // id와 idIndex 변수 선언
        String id = null;
        int idIndex = 0; // 기본적으로 첫 번째 매개변수를 수정하려는 의도로 사용됩니다.

        // LoginCheck 어노테이션에서 설정한 사용자 유형을 가져옵니다.
        String userType = loginCheck.type().toString();

        // userType에 따라 ADMIN 또는 USER로 구분하여 각각의 로그인 ID를 가져옵니다.
        switch (userType) {
            case "ADMIN": {
                // 관리자 로그인 ID를 세션에서 가져옵니다.
                id = SessionUtil.getLoginAdminId(session);
                break;
            }
            case "USER": {
                // 일반 사용자 로그인 ID를 세션에서 가져옵니다.
                id = SessionUtil.getLoginMemberId(session);
                break;
            }
        }

        // ID가 null인 경우, 즉 로그인이 되어 있지 않은 경우 예외를 발생시킵니다.
        if (id == null) {
            // 로그에 정보를 기록합니다.
            log.info(proceedingJoinPoint.toString() + "accountName :" + id);

            // HttpStatus.UNAUTHORIZED(401) 상태와 함께 예외를 던집니다.
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "로그인한 id값을 확인해주세요.") {
            };
        }

        // 메서드 인자를 수정하기 위해 현재 메서드 인자를 복사합니다.
        Object[] modifiedArgs = proceedingJoinPoint.getArgs();

        // 인자가 null이 아닌 경우, 첫 번째 인자를 로그인한 ID로 설정합니다.
        if (proceedingJoinPoint.getArgs() != null)
            modifiedArgs[idIndex] = id;

        // 원래 메서드를 호출하면서 수정된 인자 배열을 전달합니다.
        return proceedingJoinPoint.proceed(modifiedArgs);
    }
}
