package com.gainlog.userservice.repository;

import com.gainlog.userservice.model.entity.Role;
import com.gainlog.userservice.utils.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}