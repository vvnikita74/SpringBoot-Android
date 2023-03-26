package com.example.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.models.countries;
import com.example.backend.repositories.CountryRepository;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;


@RestController
@RequestMapping("/api")
public class CountryController {

    private final CountryRepository countryRepository;

    public CountryController(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @GetMapping("/countries")
    public List
    getAllCountries() {
        return (List) countryRepository.findAll();
    }

    @PostMapping("/countries")
    public ResponseEntity<Object> createCountry(@RequestBody countries country) {

        // Проверка на пустое значение поля name  в запросе
        if (Objects.equals(country.getName(), "")) {
            return new ResponseEntity<>("name must be not null", HttpStatus.BAD_REQUEST);
        }

        // Проверяем, существует ли уже запись с таким именем
        Optional<countries> existingCountry = countryRepository.findByName(country.getName());
        if (existingCountry.isPresent()) {
            System.out.println(existingCountry);
            return new ResponseEntity<>("this country already exists", HttpStatus.BAD_REQUEST);
        }

        countries nc = countryRepository.save(country);
        return new ResponseEntity<>(nc, HttpStatus.OK);
    }

    @PutMapping("/countries/{id}")
    public ResponseEntity<String> updateCountry(@PathVariable(value = "id") Long countryId,
                                                 @RequestBody countries countryDetails) {

        // Проверка на пустое значение
        if (Objects.equals(countryDetails.getName(), "")) {
            return new ResponseEntity<>("name must be not null", HttpStatus.BAD_REQUEST);
        }

        Optional<countries> existingCountry = countryRepository.findById(countryId);
        // Проверка на существование поля с переданным id
        if (existingCountry.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "country not found");
        }
        else {
            countries newCountry = existingCountry.get();
            newCountry.name = countryDetails.name;
            countryRepository.save(newCountry);
            return ResponseEntity.ok("Success");
        }
    }

    @DeleteMapping("/countries/{id}")
    public ResponseEntity<Object> deleteCountry(@PathVariable(value = "id") Long countryId) {
        Optional<countries> countryToDelete  = countryRepository.findById(countryId);
        if (countryToDelete.isEmpty()) {
            return new ResponseEntity<>("Deletion failed", HttpStatus.OK);
        }
        else {
            countryRepository.delete(countryToDelete.get());
            return new ResponseEntity<>("Deletion successful", HttpStatus.OK);
        }
    }
}
