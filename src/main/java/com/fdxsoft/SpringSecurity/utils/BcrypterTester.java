package com.fdxsoft.SpringSecurity.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BcrypterTester {

    // Metodo para generar passwords de prueba
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "AhMesAmies2506";
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("Password: " + password);
        System.out.println("Encoded Password: " + encodedPassword);
        System.out.println("Matches: " + passwordEncoder.matches(password, encodedPassword));
    }
}
