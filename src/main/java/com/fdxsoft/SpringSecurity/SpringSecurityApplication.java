package com.fdxsoft.SpringSecurity;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fdxsoft.SpringSecurity.entity.RoleEntity;
import com.fdxsoft.SpringSecurity.entity.UserEntity;
import com.fdxsoft.SpringSecurity.enums.ERole;
import com.fdxsoft.SpringSecurity.repository.UserRepository;

@SpringBootApplication
public class SpringSecurityApplication {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

	@Bean
	CommandLineRunner init() {
		return args -> {
			UserEntity userFederico = UserEntity.builder()
					.username("federico")
					.password(passwordEncoder.encode("1234"))
					.email("fedemtzesc@hotmail.com")
					.roles(Set.of(RoleEntity.builder()
							.name(ERole.valueOf(ERole.ADMIN.name()))
							.build()))
					.build();

			UserEntity userYolanda = UserEntity.builder()
					.username("yolanda")
					.password(passwordEncoder.encode("1234"))
					.email("coneja@hotmail.com")
					.roles(Set.of(RoleEntity.builder()
							.name(ERole.valueOf(ERole.USER.name()))
							.build()))
					.build();

			UserEntity userValeria = UserEntity.builder()
					.username("valeria")
					.password(passwordEncoder.encode("1234"))
					.email("valita@hotmail.com")
					.roles(Set.of(RoleEntity.builder()
							.name(ERole.valueOf(ERole.DEVELOPER.name()))
							.build()))
					.build();

			UserEntity userSebastian = UserEntity.builder()
					.username("sebastian")
					.password(passwordEncoder.encode("1234"))
					.email("sebas@hotmail.com")
					.roles(Set.of(RoleEntity.builder()
							.name(ERole.valueOf(ERole.GUEST.name()))
							.build()))
					.build();

			userRepository.save(userFederico);
			userRepository.save(userYolanda);
			userRepository.save(userValeria);
			userRepository.save(userSebastian);

		};
	}

}
