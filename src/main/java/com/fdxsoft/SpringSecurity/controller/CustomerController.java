package com.fdxsoft.SpringSecurity.controller;

/**
 * Cuando queramos salir del modo de seguridad por defecto, debemos teclear en
 * el navegador la siguiente url: http://localhost:8080/logout
 * el sistema nos presentara un mensaje para que confirmemos, le hacemos click
 * en el boton de Log Out y el sistema nos sacara.
 */

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1")
public class CustomerController {

    @GetMapping("/index")
    public String index() {
        return "Hello World";
    }

    @GetMapping("/index2")
    public String index2() {
        return "Hello World NOT SECURED";
    }
}
