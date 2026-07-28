package com.toletboards.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.toletboards.model.User;
import com.toletboards.model.Role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    long count();

    Page<User> findAll(Pageable pageable);




    Page<User> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContaining(
        String fullName,
        String email,
        String phone,
        Pageable pageable
);

Page<User> findByRole(
        Role role,
        Pageable pageable
);

Page<User> findByRoleAndFullNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCaseOrRoleAndPhoneContaining(
        Role role,
        String fullName,
        Role role2,
        String email,
        Role role3,
        String phone,
        Pageable pageable
);

 
 
    // Dashboard Stats
    long countByRole(Role role);

    long countByVerifiedTrue();
}