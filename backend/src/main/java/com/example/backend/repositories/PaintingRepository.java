package com.example.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.models.paintings;

public interface PaintingRepository extends JpaRepository<paintings, Long>{

}
