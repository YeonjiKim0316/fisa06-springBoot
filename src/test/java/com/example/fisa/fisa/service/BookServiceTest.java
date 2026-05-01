package com.example.fisa.fisa.service;

import com.example.fisa.entity.Book;
import com.example.fisa.exception.BookNotFoundException;
import com.example.fisa.repository.BookRepository;
import com.example.fisa.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

// @ExtendWith: JUnit5 테스트에 Mockito 기능을 연결하는 어노테이션입니다
// 이 어노테이션이 있어야 @Mock, @InjectMocks 등이 동작합니다
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    // @Mock: 실제 객체 대신 동작을 흉내 내는 가짜 객체(Mock)를 만듭니다
    // 실제 DB 연결 없이 "이 메서드를 호출하면 이 값을 반환해라"고 설정할 수 있습니다
    @Mock
    BookRepository bookRepository;

    // @InjectMocks: 위에서 만든 @Mock 객체들을 생성자/필드에 자동으로 주입합니다
    // → bookService 내부의 bookRepository가 위의 가짜 객체로 교체됩니다
    @InjectMocks
    BookService bookService;

    Book sampleBook;

    // @BeforeEach: 각 @Test 메서드가 실행되기 전에 매번 먼저 실행됩니다
    // 테스트마다 깨끗한 초기 상태를 만들기 위해 사용합니다
    @BeforeEach
    void setUp() {
        sampleBook = new Book();
        sampleBook.setId(1L);
        sampleBook.setTitle("스프링부트 핵심 가이드");
        sampleBook.setAuthor("플래처");
        sampleBook.setPage(900);
    }

    // @Test: 이 메서드가 테스트 케이스임을 JUnit에 알립니다
    // @DisplayName: 테스트 실패 시 콘솔에 표시될 설명을 지정합니다
    @Test
    @DisplayName("전체 책 조회 시 Repository 결과를 그대로 반환한다")
    void getAllBooks_returnsAllBooks() {
        // given: 테스트 사전 조건 — Mock 동작을 설정합니다
        // "bookRepository.findAll()이 호출되면 List.of(sampleBook)을 반환하라"
        given(bookRepository.findAll()).willReturn(List.of(sampleBook));

        // when: 테스트할 실제 동작을 실행합니다
        List<Book> result = bookService.getAllBooks();

        // then: 결과가 예상과 일치하는지 검증합니다
        // assertThat(검증대상).조건(): 조건이 거짓이면 테스트 실패로 표시됩니다
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("스프링부트 핵심 가이드");
    }

    @Test
    @DisplayName("존재하는 ID로 조회하면 책을 반환한다")
    void getBookById_existingId_returnsBook() {
        // Optional.of(sampleBook): 값이 있는 Optional을 만듭니다
        given(bookRepository.findById(1L)).willReturn(Optional.of(sampleBook));

        Optional<Book> result = bookService.getBookById(1L);

        // isPresent(): Optional 안에 값이 존재하는지 검증합니다
        assertThat(result).isPresent();
        assertThat(result.get().getAuthor()).isEqualTo("플래처");
    }

    @Test
    @DisplayName("책 저장 시 Repository의 save가 호출된다")
    void saveBook_callsRepositorySave() {
        given(bookRepository.save(sampleBook)).willReturn(sampleBook);

        Book result = bookService.saveBook(sampleBook);

        assertThat(result.getId()).isEqualTo(1L);
        // then(mock).should().method(): Mock 객체의 특정 메서드가 실제로 호출됐는지 검증합니다
        then(bookRepository).should().save(sampleBook);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 부분수정 시 BookNotFoundException이 발생한다")
    void updateBookById2_notFound_throwsException() {
        // Optional.empty(): 값이 없는 Optional — "DB에서 찾지 못함"을 표현합니다
        given(bookRepository.findById(999L)).willReturn(Optional.empty());

        // assertThatThrownBy: 람다 안의 코드가 예외를 던지는지 검증합니다
        // .isInstanceOf(): 던진 예외가 지정한 타입인지 확인합니다
        // .hasMessageContaining(): 예외 메시지에 해당 문자열이 포함되는지 확인합니다
        assertThatThrownBy(() -> bookService.updateBookById2(999L, sampleBook))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("삭제 시 Repository의 deleteById가 호출된다")
    void deleteBookById_callsRepositoryDelete() {
        bookService.deleteBookById(1L);

        // 반환값이 없는(void) 메서드는 should()로 호출 여부만 검증합니다
        then(bookRepository).should().deleteById(1L);
    }
}
