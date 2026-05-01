package com.example.fisa.controller;

import com.example.fisa.entity.Book;
import com.example.fisa.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// controller/BookController.java
@RestController
@RequestMapping("api/v1/books")
public class BookController {
    // 의존성(Dependency) bookService을 스프링이 관리하는 bean을 주입(Injection)
    // private: 클래스 바깥에서 접근 불가
    // final: 재정의 불가, 다른 사람들이 실수로 객체를 변경할 수 없도록
    private final BookService bookService;
//    BookService bookService = new BookService();

    // 생성자
    @Autowired // 생략해도 Spring이 알아서 관리
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // Optional<제너릭> 하면 값이 있으면 해당 값, 없으면 null을 리턴하도록 감싸주는 wrapper class
    @GetMapping("/{id}") // 동적으로 바뀌는 값 @PathVariable 사용
    public Optional<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public Book saveBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    // PUT은 DB에서 전체 데이터를 가져와 전체를 변경
    @PutMapping("/{id}")
    public void updateBy

    // PATCH는 DB에서 전체 데이터를 가져와서 일부 데이터만 변경
//    @PatchMapping("/{id}")
}
