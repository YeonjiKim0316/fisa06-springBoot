package com.example.fisa.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

// controller/HelloController.java
@Slf4j
@RestController // RESTApi로 통신
@RequestMapping("/hello")   // @WebServlet("/hello)
public class HelloController {

    // application.properties의 app.title 값을 주입받습니다.
    @Value("${app.title}")
    private String appTitle;

    @GetMapping // doGet 받아와서 overide - redirect / request.dispatcher(
    public String sayHello() {
        log.trace("--- 개발시 특정 흐름을 추적하기 위한 trace 단계의 log");
        log.debug("---debug 단계의 log");
        log.info("---info 단계의 log");
        log.warn("---warn 단계의 log");
        log.error("---error 단계의 log");

        return "Hello, Spring Boot!";
    }


    @GetMapping("/{id}") // test?name=값
    public String sayHello(@PathVariable String id, @RequestParam(value = "name", defaultValue = "Guest") String name) {
        return "Hello, " + id + "/" + name + "!";
    }

    @GetMapping("/value")
    public String printConfig() {
        return "설정된 타이틀: " + appTitle;
    }
}