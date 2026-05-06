//package com.example.fisa.controller;
//
//import com.example.fisa.entity.Book;
//import com.example.fisa.service.BookService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//// REST API에 설명을 달거나,
//// REST API가 아닌 경우에는 아래와 같이 @Tag와 @Operation을 사용하면 작성됩니다.
//
//@Tag(name = "swagger 테스트 API", description = "swagger 테스트를 진행하는 API")
//// controller/BookController.java
//@Controller // MVC 패턴을 위한 별도의 bean을 사용합니다.
//@RequestMapping("books") // MVC 패턴으로 서비스를 구현하더라도 테스트 자동화해서 사용하기 위해서 RestController도 함께 작성합니다.
//public class BookController {
//    // 의존성(Dependency) bookService을 스프링이 관리하는 bean을 주입(Injection)
//    // private: 클래스 바깥에서 접근 불가
//    // final: 재정의 불가, 다른 사람들이 실수로 객체를 변경할 수 없도록
//    private final BookService bookService;
////    BookService bookService = new BookService();
//
//    // 생성자
//    @Autowired // 생략해도 Spring이 알아서 관리
//    public BookController(BookService bookService) {
//        this.bookService = bookService;
//    }
//
//    @Operation(summary = "Book 정보 모두 조회", description = "Book의 전체 정보를 조회합니다.")
//    @GetMapping // request: 사용자로부터 들어오는 데이터를 관리하는 보따리, response: 사용자에게 넘겨줄 데이터를 관리하는 보따리, model: html에 넣어줄 데이터를 관리하는 보따리
//    public String getAllBooks(HttpServletRequest request, HttpServletResponse response, Model model) {
//        List<Book> books = bookService.getAllBooks();
//        model.addAttribute("books", books);
//        // 수요일: pom.xml에 maven repo에서 검색한 thymeleaf 추가하고 시작
////        		<dependency>
////                <groupId>org.springframework.boot</groupId>
////                <artifactId>spring-boot-starter-thymeleaf</artifactId>
////                </dependency>
//        return "bookmain-old"; // 전달할 html 경로 주소
//    }
//
////    // Optional<제너릭> 하면 값이 있으면 해당 값, 없으면 null을 리턴하도록 감싸주는 wrapper class
////    @ResponseBody // body에 문자열 전달
////    @Operation(summary = "Book 정보 개별 조회", description = "Book의 단권 정보를 조회합니다.")
////    @GetMapping("/{id}") // 동적으로 바뀌는 값 @PathVariable 사용
////    public Optional<Book> getBookById(@PathVariable Long id) {
////        return bookService.getBookById(id);
////    }
////
//    // MVC 패턴에서는 GET / POST MAPPING만 지원합니다.
//    @PostMapping("/{id}/delete")
//    public String deleteBook(@PathVariable Long id) {
//        bookService.deleteBook(id);
//        return "redirect:/books";
//    }
//
//
//    @PostMapping
//    // @ModelAttribute 로 모델을 통해 전달받은 값을 아규먼트로 사용
////    public Book saveBook(@RequestBody Book book) {
//    public String saveBook(HttpServletRequest request, HttpServletResponse response,
//                         @ModelAttribute Book book) {
//        bookService.saveBook(book);
//        // saveBook 을 수행한 후에 다시 GET books/ redirect
//        return "redirect:/books";
//    }
//
//
//
////    // PUT은 DB에서 전체 데이터를 가져와 전체를 변경
////    @PutMapping("/{id}")
////    public void updateByBookById(@PathVariable Long id, @RequestBody Book book){
////        book.setId(id); // controller에서 id를 함께 전달
////        bookService.saveBook(book); // bookService의 saveBook을 재사용
////    }
////
////    // PATCH는 DB에서 전체 데이터를 가져와서 일부 데이터만 변경
////    @PatchMapping("/{id}")
////    public void updateByBookById2(@PathVariable Long id, @RequestBody Book book){
////        bookService.updateByBookById2(id, book);
////    }
////
//////    {{url}}/{{path}}/select3?minPage=10&maxPage=301
////    @GetMapping("/select3")
////    public List<Book> getBookByPages(@RequestParam int minPage, @RequestParam int maxPage) {
////        return bookService.getBookByPages(minPage, maxPage);
////    }
////
////    //    - getBookByTitle
////    //    - 책이름으로 책을 검색하는 API
////    //    - // books/select1?title=책이름
////    //            - 완전일치/일부일치 여부 확인해보기
////    @GetMapping("/select1")
////    public List<Book> getBookByTitle(@RequestParam String title) {
////        return bookService.getBookByTitle(title);
////    }
////    //- getBookByTitleAndAuthor:
////    //            - 책이름과 저자로 책을 검색하는 API
////    //    - // books/select2?title=책이름&author=저자
////    @GetMapping("/select2")
////    public List<Book> getBookByTitleAndAuthor(@RequestParam String title, @RequestParam String author) {
////        return bookService.getBookByTitleAndAuthor(title, author);
////    }
////
////    //- getBookByTitleOrAuthor:
////    //            - 책이름이 일부 일치하거나 저자명이 일부일치하는 조건으로 책을 검색하는 API
////    //    - // books/select4?title=책이름&author=저자
////    @GetMapping("/select4")
////    public List<Book> getBookByTitleOrAuthor(@RequestParam String title, @RequestParam String author) {
////        return bookService.getBookByTitleOrAuthor(title, author);
////    }
//
//}
package com.example.fisa.controller;

import com.example.fisa.entity.Book;
import com.example.fisa.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

@Slf4j
@Tag(name = "swagger 테스트 API", description = "swagger 테스트를 진행하는 API")
@Controller // @RestController가 아니면 swagger 사용 불가
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // 📘 책 목록 조회
    @GetMapping
    public String getAllBooks(HttpServletRequest request, Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        log.info("{} {} - 책 전체 목록 조회: {}권", request.getMethod(), request.getRequestURI(), books.size());
//        return "bookmain-old";
         return "bookmain"; // bookmain.html로 model 객체 가지고 이동
    }

    // ➕ 책 추가 폼
    @GetMapping("/add")
    public String addBookForm(HttpServletRequest request, Model model) {
        model.addAttribute("book", new Book()); // 폼 바인딩용 빈 객체
        log.info("{} {} - 책 추가 폼 진입", request.getMethod(), request.getRequestURI());
        return "fragments/form-add"; // bookmain.html + form-add fragment
    }

    @GetMapping("/{id}") // 동적으로 바뀌는 값 @PathVariable 사용
    public Optional<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    // 💾 책 저장
    @PostMapping
    public String saveBook(HttpServletRequest request, @ModelAttribute Book book) {
        bookService.saveBook(book);
        log.info("{} {} - 책 추가: {} / {}", request.getMethod(), request.getRequestURI(), book.getTitle(), book.toString());
        return "redirect:/books"; // 저장 후 목록으로 리다이렉트
    }

    // 🔍 책 검색 폼
    @GetMapping("/search")
    public String searchForm() {
        return "form-search"; // bookmain.html + form-search fragment
    }

    // 📊 조건 검색
    @GetMapping("/select4")
    public String searchBooks(@RequestParam(defaultValue = "") String title,
                              @RequestParam(defaultValue = "") String author,
                              @RequestParam(defaultValue = "or") String searchType,
                              Model model) {
        List<Book> results;
        boolean hasTitle = !title.isBlank();
        boolean hasAuthor = !author.isBlank();

        if (hasTitle && hasAuthor) {
            if ("and".equals(searchType)) {
                results = bookService.getBookByTitleAndAuthor(title, author);
            } else {
                results = bookService.getBookByTitleAndAuthor(title, author);
            }
        } else if (hasTitle) {
            results = bookService.getBookByTitle(title);
//        } else if (hasAuthor) {
//            results = bookService.getBookByAuthor(author);
        } else {
            results = bookService.getAllBooks();
        }

        model.addAttribute("books", results);
        return "bookmain";
    }

    // 🗑 삭제
    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBookForm(@PathVariable Long id, Model model) {
        Optional<Book> bookOptional = bookService.getBookById(id);
        if (bookOptional.isPresent()) {
            model.addAttribute("book", bookOptional.get());
            return "form-edit";
        } else {
            return "redirect:/books"; // 존재하지 않으면 목록으로
        }
    }

    // ✏️ 수정
    @PostMapping("/{id}/update")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book) {
        book.setId(id);
        bookService.saveBook(book);
        return "redirect:/books";
    }

}
