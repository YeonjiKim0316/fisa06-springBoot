package com.example.fisa.controller;

import com.example.fisa.entity.Book;
import com.example.fisa.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// REST API에 설명을 달거나, REST API가 아닌 경우에는 아래와 같이 @Tag와 @Operation을 사용하면 작성됩니다.

@Tag(name = "swagger 테스트 API", description = "swagger 테스트를 진행하는 API")
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

    @Operation(summary = "Book 정보 모두 조회", description = "Book의 전체 정보를 조회합니다.")
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // Optional<제너릭> 하면 값이 있으면 해당 값, 없으면 null을 리턴하도록 감싸주는 wrapper class
    @Operation(summary = "Book 정보 개별 조회", description = "Book의 단권 정보를 조회합니다.")
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
    //    - // books/select1?title=책이름
    //            - 완전일치/일부일치 여부 확인해보기
    @GetMapping("/select1")
    public List<Book> getBookByTitle(@RequestParam String title) {
        return bookService.getBookByTitle(title);
    }
    //- getBookByTitleAndAuthor:
    //            - 책이름과 저자로 책을 검색하는 API
    //    - // books/select2?title=책이름&author=저자
    @GetMapping("/select2")
    public List<Book> getBookByTitleAndAuthor(@RequestParam String title, @RequestParam String author) {
        return bookService.getBookByTitleAndAuthor(title, author);
    }

    //- getBookByTitleOrAuthor:
    //            - 책이름이 일부 일치하거나 저자명이 일부일치하는 조건으로 책을 검색하는 API
    //    - // books/select4?title=책이름&author=저자
    @GetMapping("/select4")
    public List<Book> getBookByTitleOrAuthor(@RequestParam String title, @RequestParam String author) {
        return bookService.getBookByTitleOrAuthor(title, author);
    }

}
