package com.fdxsoft.SpringSecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fdxsoft.SpringSecurity.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    // Esta es otra forma de hacer findByUsername con @Query
    @Query("select u from UserEntity u where u.username = :username")
    Optional<UserEntity> getUserName(String username);

    Optional<UserEntity> findByEmail(String email);
}
