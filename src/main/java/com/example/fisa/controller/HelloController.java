package com.example.fisa.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

// controller/HelloController.java
@RestController // RESTApi로 통신
@RequestMapping("/hello")   // @WebServlet("/hello)
public class HelloController {

    // application.properties의 app.title 값을 주입받습니다.
    @Value("${app.title}")
    private String appTitle;

    @GetMapping // doGet 받아와서 overide - redirect / request.dispatcher(
    public String sayHello() {
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