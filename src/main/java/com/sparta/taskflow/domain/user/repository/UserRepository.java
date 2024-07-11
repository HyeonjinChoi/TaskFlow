package com.sparta.taskflow.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.taskflow.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
