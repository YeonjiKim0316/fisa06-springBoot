package com.example.fisa.repository;

import com.example.fisa.entity.Book;
import org.springframework.stereotype.Repository;


// 실제 DB에 접근해서 데이터를 가져오기 위한 쿼리 / 쿼리에 대응되는 메서드 <엔티티명, 엔티티의 id명>
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<Book, Long>을 상속하면:
//   - 첫 번째 타입(Book): 다룰 Entity 클래스
//   - 두 번째 타입(Long): 해당 Entity의 PK 타입
//   - save(), findById(), findAll(), deleteById(), count() 등 기본 CRUD 메서드가 자동 제공됩니다
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // 여기에 필요한 경우 사용자 정의 쿼리를 추가할 수 있습니다.
    // ─────────────────────────────────────────────
    // JPA 메서드 네이밍 규칙으로 쿼리 자동 생성
    // "find + By + 필드명 + 조건키워드" 형식으로 작성합니다
    // ─────────────────────────────────────────────

    // ─────────────────────────────────────────────
    // JPQL(Java Persistence Query Language): 테이블명 대신 Entity 클래스명을 사용하는 쿼리
    // SQL: SELECT * FROM book WHERE ...
    // JPQL: SELECT b FROM Book b WHERE ...  ← Book은 클래스명, b는 별칭(alias)
    //
    // @Param("title"): 쿼리의 :title 자리에 메서드 파라미터 title 값을 바인딩합니다
    // %:title%  → LIKE '%검색어%' 와 동일한 부분 일치 표현
    // ─────────────────────────────────────────────
}