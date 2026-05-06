package com.example.fisa.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// @RestControllerAdvice: 모든 @RestController에서 예외가 발생하면 이 클래스가 가로채서 처리합니다
// = @ControllerAdvice(모든 컨트롤러 대상) + @ResponseBody(반환값을 JSON으로 직렬화)
// 각 Controller마다 try-catch를 작성하는 대신, 예외 처리를 이 한 곳에 모아둡니다
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler: 괄호 안의 예외 타입이 발생했을 때 이 메서드를 실행합니다
    // BookNotFoundException이 throw되면 자동으로 여기로 흘러옵니다
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBookNotFound(BookNotFoundException e) {
        // ResponseEntity: HTTP 상태 코드와 응답 본문을 함께 설정할 수 있는 클래스
        // Map.of("error", e.getMessage()) → { "error": "Book not found: id=999" } 형태의 JSON으로 변환됩니다
        return ResponseEntity.status(HttpStatus.NOT_FOUND)          // 404
                .body(Map.of("error", e.getMessage()));
    }

    // 위에서 처리하지 못한 모든 예외의 최종 안전망입니다
    // Exception.class는 모든 예외의 최상위 클래스이므로 가장 넓은 범위를 잡습니다
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
                .body(Map.of("error", "Internal server error"));
    }
}