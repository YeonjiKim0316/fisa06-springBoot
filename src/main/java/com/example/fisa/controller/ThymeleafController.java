package com.example.fisa.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/sample")
public class ThymeleafController {

    @GetMapping
    public String thymeleafSample(HttpServletRequest request,
                                  HttpServletResponse response,
                                  HttpSession session,
                                  Model model) {

        // 세션 설정
        session.setAttribute("sessionMessage", "세션에 저장된 메시지입니다.");

        // 쿠키 설정
        Cookie cookie = new Cookie("myCookie", "쿠키값입니다");
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1시간
        response.addCookie(cookie);

        // 모델에 사용자 정보 및 리스트 추가
        model.addAttribute("user", new User("이순신", 25));
        model.addAttribute("items", List.of("사과", "바나나", "체리"));

        // 세션에서 메시지 가져오기
        String sessionMessage = (String) session.getAttribute("sessionMessage");
        model.addAttribute("sessionMessage", sessionMessage);

        // 쿠키 읽기
        String cookieValue = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("myCookie".equals(c.getName())) {
                    cookieValue = c.getValue();
                    break;
                }
            }
        }
        model.addAttribute("cookieMessage", cookieValue);

        return "sample";
    }

    @PostMapping("/submit")
    public String submitForm(@RequestParam("username") String newUsername, Model model) {

        // 1. 전달받은 새 사용자 이름 출력 (로깅)
        System.out.println("폼 제출됨 - 새로운 사용자 이름: " + newUsername);

        // 2. 변경된 사용자 정보로 모델을 업데이트
        // (실제 애플리케이션에서는 DB에 업데이트하는 서비스 로직이 실행될 것입니다.)
        User updatedUser = new User(newUsername, 25);

        model.addAttribute("user", updatedUser);
        model.addAttribute("items", List.of("사과", "바나나", "체리")); // 다른 모델 데이터 유지

        // 제출 성공 메시지 추가
        model.addAttribute("sessionMessage", "사용자 이름이 \"" + newUsername + "\" 으로 변경되었습니다.");

        // POST 요청 후에도 동일한 뷰(sample.html)를 다시 렌더링
        return "sample";

    }
}

@AllArgsConstructor
@Data
class User {
    String name;
    int age;
}