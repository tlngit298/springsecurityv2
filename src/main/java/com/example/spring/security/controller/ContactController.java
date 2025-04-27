package com.example.spring.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/contact")
public class ContactController {

    @GetMapping
    public String getContact() {
        return "This is my contact.";
    }
}
