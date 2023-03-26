package com.example.backend.repositories;


import com.example.backend.models.countries;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CountryRepository  extends CrudRepository<countries, Long> {
    @Query("SELECT c FROM countries c WHERE c.name = :name")
    public Optional<countries> findByName(@Param("name") String name);

    @Query("SELECT c FROM countries c WHERE c.id = :id")
    public Optional<countries> findById(@Param("id") Long id);
}