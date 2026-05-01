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


    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }


    @PostMapping
    public Book saveBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    // PUT은 DB에서 전체 데이터를 가져와 전체를 변경
    @PutMapping("/{id}")
    public void updateByBookById(@PathVariable Long id, @RequestBody Book book){
        book.setId(id); // controller에서 id를 함께 전달
        bookService.saveBook(book); // bookService의 saveBook을 재사용
    }

    // PATCH는 DB에서 전체 데이터를 가져와서 일부 데이터만 변경
    @PatchMapping("/{id}")
    public void updateByBookById2(@PathVariable Long id, @RequestBody Book book){
        bookService.updateByBookById2(id, book);
    }

//    {{url}}/{{path}}/select3?minPage=10&maxPage=301
    @GetMapping("/select3")
    public List<Book> getBookByPages(@RequestParam int minPage, @RequestParam int maxPage) {
        return bookService.getBookByPages(minPage, maxPage);
    }

//    - getBookByTitle
//    - 책이름으로 책을 검색하는 API
//    - // books/select?title=책이름
//            - 완전일치/일부일치 여부 확인해보기
//- getBookByTitleAndAuthor:
//            - 책이름과 저자로 책을 검색하는 API
//    - // books/select?title=책이름&author= 저자
}
