package com.example.fisa.exception;

public class BookNotFoundException extends Exception {
    // 생성자만 하나 만들어서 예외 발생시 메시지를 작성합니다.

    public BookNotFoundException(Long id) {
        super("Book not found id = " + id);
    }
}
