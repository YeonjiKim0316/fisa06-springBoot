package com.example.fisa.fisa.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import com.example.fisa.entity.Book;
import com.example.fisa.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // 실제 DB에서 테스트하겠다는 의미
public class BookRepoTest {

    @Autowired
    BookRepository repository;

    @Test
    public void insertTest() {
        // given
         Book book = new Book();
         book.setTitle("짱구는 못말려");
         book.setAuthor("신짱구");
         book.setGenre("코미디");
         book.setPage(120);
         book.setPrice(15000);

// 빌더 패턴 사용시 Entity에 
// @Builder @NoArgsConstructor @AllArgsConstructor 추가
//        // given
//        Book book = Book.builder()
//                .title("짱구는 못말려")
//                .author("신짱구")
//                .genre("코미디")
//                .page(120)
//                .price(15000)
//                .build();


        // 저장
        Book savedBook = repository.saveAndFlush(book);

        // when
        Optional<Book> selected = repository.findById(savedBook.getId());

        // then
        assertTrue(selected.isPresent());
        assertEquals("짱구는 못말려", selected.get().getTitle());
        assertEquals("신짱구", selected.get().getAuthor());
    }
}