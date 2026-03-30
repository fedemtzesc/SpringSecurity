package com.fdxsoft.SpringSecurity.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fdxsoft.SpringSecurity.controller.dto.CreateUserDTO;
import com.fdxsoft.SpringSecurity.entity.RoleEntity;
import com.fdxsoft.SpringSecurity.entity.UserEntity;
import com.fdxsoft.SpringSecurity.enums.ERole;
import com.fdxsoft.SpringSecurity.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class PrincipalController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World Not Secured";
    }

    @GetMapping("/index")
    public String helloSecured() {
        return "Hello World Secured";
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        Map<String, Object> response = new LinkedHashMap<>();

        Set<RoleEntity> roles = createUserDTO.getRoles().stream()
                .map(role -> RoleEntity.builder().name(ERole.valueOf(role)).build())
                .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .username(createUserDTO.getUsername())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .email(createUserDTO.getEmail())
                .roles(roles)
                .build();

        try {
            userRepository.save(userEntity);
            response.put("status", HttpStatus.CREATED.value());
            response.put("message", "Usuario creado con éxito");
            response.put("data", userEntity);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", "Ocurrio un error al intentar crear un nuevo usuario");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam String id) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            if (!userRepository.existsById(Long.parseLong(id))) {
                throw new Exception("No se encontró el usuario con ID: " + id);
            }
            userRepository.deleteById(Long.parseLong(id));
            response.put("status", HttpStatus.CREATED.value());
            response.put("message", "Usuario eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", "Ocurrio un error al intentar eliminar el usuario con id: " + id);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}
