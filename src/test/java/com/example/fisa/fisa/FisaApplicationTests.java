package com.example.fisa.fisa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // application-test.properties의 환경으로 테스트해
class FisaApplicationTests {

	@Test
	void contextLoads() {
	}

}
