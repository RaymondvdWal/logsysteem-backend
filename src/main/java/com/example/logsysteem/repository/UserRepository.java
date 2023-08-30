package com.example.logsysteem.repository;

import com.example.logsysteem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
