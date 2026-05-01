package com.example.fisa.service;

import com.example.fisa.entity.Book;
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


}
