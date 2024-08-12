package com.dustin.boardserver.exception.handler;

import com.dustin.boardserver.dto.response.CommonResponse;
import com.dustin.boardserver.exception.BoardServerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice: 이 클래스가 스프링의 전역 예외 처리 핸들러임을 나타냅니다.
// 컨트롤러 전반에서 발생하는 예외를 이 클래스에서 처리할 수 있습니다.
@RestControllerAdvice
public class CustomExceptionHandler {

    // RuntimeException을 처리하는 메서드입니다.
    // @ExceptionHandler: 이 메서드는 RuntimeException 타입의 예외가 발생할 때 호출됩니다.
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeExceptionException(RuntimeException ex) {
        // CommonResponse 객체를 생성하여 예외 메시지를 포함한 응답을 생성합니다.
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "RuntimeException", ex.getMessage(), ex.getMessage());
        // ResponseEntity를 반환하여 클라이언트에게 HTTP 응답을 전송합니다.
        return new ResponseEntity<>(commonResponse, new HttpHeaders(), commonResponse.getStatus());
    }

    // BoardServerException을 처리하는 메서드입니다.
    // @ExceptionHandler: 이 메서드는 BoardServerException 타입의 예외가 발생할 때 호출됩니다.
    @ExceptionHandler({BoardServerException.class})
    public ResponseEntity<Object> handleBoardServerException(BoardServerException ex) {
        // CommonResponse 객체를 생성하여 예외 메시지를 포함한 응답을 생성합니다.
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "BoardServerException", ex.getMessage(), ex.getMessage());
        // ResponseEntity를 반환하여 클라이언트에게 HTTP 응답을 전송합니다.
        return new ResponseEntity<>(commonResponse, new HttpHeaders(), commonResponse.getStatus());
    }

    // 모든 Exception을 처리하는 메서드입니다.
    // @ExceptionHandler: 이 메서드는 Exception 타입의 예외가 발생할 때 호출됩니다.
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR): 이 메서드는 500 Internal Server Error 상태 코드를 반환합니다.
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> defaultException(Exception ex) {
        // CommonResponse 객체를 생성하여 예외 메시지를 포함한 응답을 생성합니다.
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "Exception", ex.getMessage(), ex.getMessage());
        // ResponseEntity를 반환하여 클라이언트에게 HTTP 응답을 전송합니다.
        return new ResponseEntity<>(commonResponse, new HttpHeaders(), commonResponse.getStatus());
    }

}
