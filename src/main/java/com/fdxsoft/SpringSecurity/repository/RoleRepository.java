package com.fdxsoft.SpringSecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdxsoft.SpringSecurity.entity.RoleEntity;
import com.fdxsoft.SpringSecurity.enums.ERole;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(ERole name);
}
