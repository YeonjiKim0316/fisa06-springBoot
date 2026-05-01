package com.example.fisa.fisa.controller;

import com.example.fisa.entity.Book;
import com.example.fisa.exception.BookNotFoundException;
import com.example.fisa.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest: 슬라이스 테스트 — Controller 레이어만 Spring 컨텍스트에 올립니다
// Service, Repository, DB는 로드하지 않아 전체 @SpringBootTest 보다 훨씬 빠릅니다
// 괄호 안에 테스트할 Controller 클래스를 지정합니다
@WebMvcTest(RestBookController.class)
class RestBookControllerTest {

    // @Autowired: Spring이 자동으로 주입해주는 MockMvc 객체입니다
    // MockMvc: 실제 서버를 실행하지 않고 HTTP 요청/응답을 시뮬레이션합니다
    @Autowired
    MockMvc mockMvc;

    // @MockitoBean: @WebMvcTest 컨텍스트에 가짜 Bean을 등록합니다
    // @Mock과 달리 Spring 컨텍스트 안에 주입되어 Controller가 사용할 수 있습니다
    // Spring Boot 3.4부터 기존 @MockBean이 deprecated → @MockitoBean으로 교체
    // import: org.springframework.test.context.bean.override.mockito.MockitoBean
    @MockitoBean
    BookService bookService;

    // ObjectMapper: Java 객체 ↔ JSON 문자열 변환 유틸리티
    // objectMapper.writeValueAsString(obj): 객체를 JSON 문자열로 직렬화합니다
    @Autowired
    ObjectMapper objectMapper;

    Book sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = new Book();
        sampleBook.setId(1L);
        sampleBook.setTitle("스프링부트 핵심 가이드");
        sampleBook.setAuthor("플래처");
        sampleBook.setPage(900);
    }

    @Test
    @DisplayName("GET /rest-books 는 책 목록을 JSON으로 반환한다")
    void getAllBooks_returns200WithJson() throws Exception {
        given(bookService.getAllBooks()).willReturn(List.of(sampleBook));

        // mockMvc.perform(): 가상의 HTTP 요청을 실행합니다
        // get("/api/v1/books"): GET /api/v1/books 요청을 만듭니다
        mockMvc.perform(get("/api/v1/books"))
                // andExpect(): 응답 결과를 검증합니다
                .andExpect(status().isOk())                                       // HTTP 상태 코드 200
                .andExpect(jsonPath("$[0].title").value("스프링부트 핵심 가이드")) // JSON 배열 첫 번째 요소의 title 검증
                .andExpect(jsonPath("$[0].author").value("플래처"));              // $: 루트, [0]: 첫 번째 요소
    }

    @Test
    @DisplayName("GET /api/v1/books/{id} 존재하는 ID 조회 시 200 반환")
    void getBookById_existing_returns200() throws Exception {
        given(bookService.getBookById(1L)).willReturn(Optional.of(sampleBook));

        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))                          // $.id: 루트 객체의 id 필드
                .andExpect(jsonPath("$.title").value("스프링부트 핵심 가이드"));
    }

    @Test
    @DisplayName("GET /api/v1/books/{id} 없는 ID 조회 시 404 반환")
    void getBookById_notFound_returns404() throws Exception {
        // willThrow: 이 메서드 호출 시 예외를 던지도록 설정합니다
        // → Controller의 orElseThrow()가 이 예외를 받아 GlobalExceptionHandler로 전달합니다
        given(bookService.getBookById(999L)).willThrow(new BookNotFoundException(999L));

        mockMvc.perform(get("/api/v1/books/999"))
                .andExpect(status().isNotFound())         // HTTP 404
                .andExpect(jsonPath("$.error").exists()); // 응답 JSON에 "error" 키가 있는지 확인
    }

    @Test
    @DisplayName("POST /api/v1/books 책 저장 시 200과 저장된 책 반환")
    void saveBook_returns200WithSavedBook() throws Exception {
        // any(Book.class): 어떤 Book 객체가 넘어와도 sampleBook을 반환하라는 설정
        // → 테스트에서 정확히 어떤 객체가 올지 모를 때 사용합니다
        given(bookService.saveBook(any(Book.class))).willReturn(sampleBook);

        // Java 15+ Text Block(""" """): 여러 줄 문자열을 깔끔하게 작성합니다
        String requestJson = """
                {
                    "title": "FISAAI-BOOKAPP",
                    "author": "김연지",
                    "page": 900,
                    "genre": "IT",
                    "price": 30000
                }
                """;

        mockMvc.perform(post("/api/v1/books")
                // contentType: 요청 본문의 형식을 JSON으로 지정합니다 (Content-Type: application/json)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("스프링부트 핵심 가이드"));
    }

    @Test
    @DisplayName("DELETE /api/v1/books/{id} 삭제 요청 시 200 반환")
    void deleteBook_returns200() throws Exception {
        // willDoNothing(): void 반환 메서드에 "아무것도 하지 말라"고 설정합니다
        willDoNothing().given(bookService).deleteBookById(1L);

        mockMvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isOk());
    }
}
