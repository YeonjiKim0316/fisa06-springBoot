package com.example.fisa.controller;

import com.example.fisa.entity.Book;
import com.example.fisa.exception.BookNotFoundException;
import com.example.fisa.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

// Spring MVC는 기본적으로 MVC 패턴을 따르는 전통적 웹 앱(뷰 렌더링)을 지원하지만
// @RestController나 @ResponseBody를 통해 동일 프레임워크 내에서 RESTful API를 별도로 구현한 엔드포인트도 가지는 경우가 많습니다.
// 이는 테스트(예: Postman, JUnit 통합 테스트), 모바일/프론트엔드 연동, 마이크로서비스 간 통신 등 실무적 이유 때문입니다.
// @Slf4j: log 필드를 자동 생성합니다 (log.info(), log.error() 등으로 로그를 남길 수 있습니다)
@Slf4j
// @RestController: JSON을 반환하는 REST API 컨트롤러입니다
@RestController
@RequestMapping("/api/v1/books") // 버전 및 api 명시, 명사/복수형 사용
public class RestBookController {

    // @Autowired: Spring이 BookService 객체를 자동으로 찾아 주입해줍니다 (생성자 주입 시 생략 가능)
    private final BookService bookService;

    // 생성자 주입(권장): final 필드를 보장하고 테스트 시 직접 주입이 쉽습니다
    public RestBookController(BookService bookService) {
        this.bookService = bookService;
    }

    // ─────────────────────────────────────────────
    // GET /rest-books → 전체 책 목록 조회
    // ─────────────────────────────────────────────
    @GetMapping
    public List<Book> getAllBooks(HttpServletRequest request) {
        log.info("{} {} - 전체 조회 요청", request.getMethod(), request.getRequestURI());
        return bookService.getAllBooks();
    }

    // ─────────────────────────────────────────────
    // POST /rest-books → 책 저장
    // ─────────────────────────────────────────────
    // @RequestBody: HTTP 요청 본문의 JSON을 Book Entity 객체로 자동 변환합니다
    // 예) { "title":"스프링", "author":"김연지", "page":300 } → Book 객체
    @PostMapping
    public Book saveBook(HttpServletRequest request, @RequestBody Book book) {
        log.info("{} {} - 저장 요청: title={}", request.getMethod(), request.getRequestURI(), book.getTitle());
        book.setId(null); // 신규 등록 시 id를 null로 강제하여 INSERT를 보장합니다
        return bookService.saveBook(book);
    }

    // ─────────────────────────────────────────────
    // GET /rest-books/{id} → 단건 조회
    // ─────────────────────────────────────────────
    // @PathVariable: URL 경로의 {id} 부분을 Long 타입 변수로 받습니다
    // 예) GET /rest-books/5 → id = 5L
    @GetMapping("/{id}")
    public Book getBookById(HttpServletRequest request, @PathVariable Long id) {
        log.info("{} {} - 단건 조회 요청", request.getMethod(), request.getRequestURI());
        // .orElseThrow(): Optional이 비어 있으면(책이 없으면) 예외를 던집니다
        // → GlobalExceptionHandler가 BookNotFoundException을 잡아 HTTP 404로 응답합니다
        return bookService.getBookById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    // ─────────────────────────────────────────────
    // DELETE /rest-books/{id} → 삭제
    // ─────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public void deleteBookById(HttpServletRequest request, @PathVariable Long id) {
        log.info("{} {} - 삭제 요청", request.getMethod(), request.getRequestURI());
        bookService.deleteBookById(id);
    }

    // ─────────────────────────────────────────────
    // PUT /rest-books/{id} → 전체 수정 (모든 필드 교체)
    // ─────────────────────────────────────────────
    @PutMapping("/{id}")
    public void updateBookById(HttpServletRequest request, @PathVariable Long id,
            @RequestBody Book book) {
        log.info("{} {} - 전체 수정 요청", request.getMethod(), request.getRequestURI());
        book.setId(id); // id를 직접 설정해야 JPA가 INSERT 대신 UPDATE를 수행합니다
        bookService.saveBook(book); // save()는 id가 있으면 UPDATE, 없으면 INSERT
    }

    // ─────────────────────────────────────────────
    // PATCH /rest-books/{id} → 부분 수정 (변경된 필드만 교체)
    // PUT과의 차이: PUT은 모든 필드를 교체, PATCH는 보낸 필드만 변경합니다
    // ─────────────────────────────────────────────
    @PatchMapping("/{id}")
    public void updateBookById2(HttpServletRequest request, @PathVariable Long id,
            @RequestBody Book book) {
        log.info("{} {} - 부분 수정 요청", request.getMethod(), request.getRequestURI());
        bookService.updateBookById2(id, book);
    }

    // ─────────────────────────────────────────────
    // 검색 API들 (쿼리 파라미터 사용)
    // @RequestParam: ?key=value 형태의 URL 파라미터를 받습니다
    // ─────────────────────────────────────────────

    // GET /rest-books/select1?title=스프링부트&author=장정우 → 제목 AND 저자로 정확히 검색
    @GetMapping("/select1")
    public List<Book> getBookByTitleAndAuthor(@RequestParam String title, @RequestParam String author) {
        return bookService.getBookByTitleAndAuthor(title, author);
    }

    // GET /rest-books/select2?title=스프링 → 제목에 키워드가 포함된 책 검색 (부분 일치)
    @GetMapping("/select2")
    public List<Book> getBookByTitle(@RequestParam String title) {
        return bookService.getBookByTitle(title);
    }

    // GET /rest-books/select3?minPage=100&maxPage=500 → 페이지 수 범위로 검색
    @GetMapping("/select3")
    public List<Book> getBookByPage(@RequestParam int minPage, @RequestParam int maxPage) {
        return bookService.getBookByPage(minPage, maxPage);
    }

    // GET /rest-books/select4?title=SQL&author=코딩맨 → 제목 OR 저자로 검색
    @GetMapping("/select4")
    public List<Book> getBookByTitleOrAuthor(@RequestParam String title, @RequestParam String author) {
        return bookService.getBookByTitleOrAuthor(title, author);
    }
}
