package com.fdxsoft.SpringSecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestRolesController {

    @GetMapping("/accessAdmin")
    public String accessAdmin() {
        return "Hola! Has accedido con rol de ADMIN";
    }

    @GetMapping("/accessUser")
    public String accessUser() {
        return "Hola! Has accedido con rol de USER";
    }

    @GetMapping("/accessDeveloper")
    public String accessDeveloper() {
        return "Hola! Has accedido con rol de DEVELOPER";
    }

    @GetMapping("/accessGuest")
    public String accessGuest() {
        return "Hola! Has accedido con rol de GUEST";
    }
}
