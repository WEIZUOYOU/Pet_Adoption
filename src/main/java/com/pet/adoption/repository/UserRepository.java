package com.pet.adoption.repository;

import com.pet.adoption.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByAccount(String account);
    User findByPhone(String phone);
}