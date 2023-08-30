package com.example.logsysteem.repository;

import com.example.logsysteem.model.Malfunction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MalfunctionRepository extends JpaRepository<Malfunction, Long> {
}
