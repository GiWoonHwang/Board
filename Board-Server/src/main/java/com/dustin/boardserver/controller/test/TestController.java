package com.dustin.boardserver.controller.test;

import com.dustin.boardserver.exception.BoardServerException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController: 이 클래스가 스프링 MVC의 REST 컨트롤러임을 나타냅니다.
// 이 클래스의 메서드들은 JSON 또는 XML 형식의 응답을 반환할 수 있습니다.
@RestController

// @Log4j2: 이 클래스에서 Log4j2를 사용하여 로그를 기록할 수 있게 합니다.
@Log4j2
public class TestController {

    // @GetMapping: 이 메서드는 HTTP GET 요청을 처리합니다.
    // "log-test" 엔드포인트로 GET 요청이 들어오면 이 메서드가 호출됩니다.
    @GetMapping("log-test")
    public void logTest() {
        // 로그의 여러 레벨로 메시지를 기록합니다.
        log.fatal("FATAL");  // 가장 심각한 로그 수준, 애플리케이션이 중단될 수 있는 치명적인 상황을 기록합니다.
        log.error("ERROR");  // 에러 발생을 기록합니다. 오류로 인해 애플리케이션이 영향을 받는 경우 사용됩니다.
        log.warn("WARN");    // 경고 수준의 로그, 문제를 일으킬 수 있는 잠재적인 이슈를 기록합니다.
        log.info("INFO");    // 일반적인 정보 수준의 로그, 애플리케이션 실행 과정의 주요 정보를 기록합니다.
        log.debug("DEBUG");  // 디버그 수준의 로그, 상세한 디버깅 정보를 기록합니다. 개발 또는 디버깅 중에 주로 사용됩니다.
        log.trace("TRACE");  // 가장 상세한 로그 수준, 매우 세밀한 트레이스 정보를 기록합니다.
    }

    // @GetMapping: 이 메서드는 "exception-test" 엔드포인트로 들어오는 GET 요청을 처리합니다.
    @GetMapping("exception-test")
    public void exceptionTest() {
        try {
            // 강제로 예외를 발생시킵니다.
            throw new Exception();
        } catch (Exception e) {
            // 발생한 예외를 잡아서 사용자 정의 예외인 BoardServerException으로 변환하여 던집니다.
            // HTTP 상태 코드는 OK(200)로 설정되고, 발생한 예외의 메시지를 함께 전달합니다.
            throw new BoardServerException(HttpStatus.OK, e.getMessage());
        }
    }
}
