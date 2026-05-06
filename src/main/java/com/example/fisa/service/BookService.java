package com.example.fisa.service;

import com.example.fisa.entity.Book;
import com.example.fisa.exception.BookNotFoundException;
import com.example.fisa.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 비즈니스 로직 자체를 담당하는 컴포넌트
@Service
public class BookService {

    // final: 한 번 주입된 후 절대 바뀌지 않음을 보장합니다 (불변성)
    private final BookRepository bookRepository;

    // 생성자 주입: Spring이 BookRepository 구현체를 찾아 자동으로 넘겨줍니다
    @Autowired // Bean을 통한 의존성 주입(생략 가능)
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll(); // repository에 SpringDataJPA의 규칙으로 작성해두면 따로 쿼리를 작성할 필요 x
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }


    public Book updateByBookById2(Long id, Book book) {
            // orElseThrow: Optional이 비어 있으면(id에 해당하는 책이 없으면) 예외를 던집니다
            // → 예전 코드의 orElse(null)은 existingBook.setTitle() 호출 시 NullPointerException 위험이 있었습니다
            Book existingBook = bookRepository.findById(id)
                    .orElseThrow(() -> new BookNotFoundException(id));
        // 요청에 포함된 필드만 덮어쓴다
        // 요청에 포함된 필드만 골라서 덮어씁니다 (null이면 변경 안 함)
        // int 기본형인 page는 null이 없으므로 0(기본값)일 때를 "변경 없음"으로 처리합니다
        if (book.getTitle() != null) {
            existingBook.setTitle(book.getTitle());
        }
        if (book.getAuthor() != null) {
            existingBook.setAuthor(book.getAuthor());
        }
        if (book.getPage() != 0) {
            existingBook.setPage(book.getPage());
        }
        if (book.getGenre() != null) {
            existingBook.setGenre(book.getGenre());
        }
        if (book.getPrice() != 0) {
            existingBook.setPrice(book.getPrice());
        }

        return bookRepository.save(existingBook);
    }

    public List<Book> getBookByPages(int minPage, int maxPage) {
        return bookRepository.findByPageBetween(minPage, maxPage);
    }

    public List<Book> getBookByTitle(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    public List<Book> getBookByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleContainingAndAuthorContaining(title, author);
    }

    public List<Book> getBookByTitleOrAuthor(String title, String author) {
        return bookRepository.findByTitleContainingOrAuthorContaining(title, author);

    }
}
