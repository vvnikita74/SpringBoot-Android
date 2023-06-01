package com.example.backend.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.CrossOrigin;
import com.example.backend.models.artists;
import com.example.backend.models.countries;
import com.example.backend.repositories.CountryRepository;

import java.util.*;
import com.example.backend.tools.DataValidationException;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class CountryController {

    private final CountryRepository countryRepository;

    public CountryController(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @GetMapping("/countries")
    public Page <countries> getAllCountries(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return countryRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }
    
    @GetMapping("/countries/{id}")
    public ResponseEntity<Object> getCountry(@PathVariable(value="id") Long countryId) throws DataValidationException {
        countries country = countryRepository.findById(countryId)
        .orElseThrow(() -> new DataValidationException("Страна с таким индексом не найдена"));
    return ResponseEntity.ok(country);
    }


    @GetMapping("/countries/{id}/artists")
    public ResponseEntity<List<artists>> getCountryArtists(@PathVariable(value = "id") Long country_id) {
        Optional<countries> cc = countryRepository.findById(country_id);
        if (cc.isEmpty()) { return ResponseEntity.ok(new ArrayList<>()); }
        return ResponseEntity.ok(cc.get().artists);
    }

    
    @PostMapping("/countries")
    public ResponseEntity<Object> createCountry(@Valid @RequestBody countries country) throws DataValidationException {
        try {
            countries nc = countryRepository.save(country);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex) {
            if (ex.getMessage().contains("countries.name_UNIQUE"))
                throw new DataValidationException("Эта страна уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    
    @PutMapping("/countries/{id}")
    public ResponseEntity<Object> updateCountry(@PathVariable(value = "id") Long countryId, @RequestBody countries countryDetails) throws DataValidationException {
        try {
            countries country = countryRepository.findById(countryId)
                .orElseThrow(() -> new DataValidationException("Страна с таким индексом не найдена"));
            country.name = countryDetails.name;
            countryRepository.save(country);
            return ResponseEntity.ok(country);
        }
        catch (Exception ex) {
            if (ex.getMessage().contains("countries.name_UNIQUE"))
                throw new DataValidationException("Эта страна уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @DeleteMapping("/countries/{id}")
    public ResponseEntity<String> deleteCountry(@PathVariable(value = "id") Long countryId) {
        Optional<countries> countryToDelete  = countryRepository.findById(countryId);
        if (countryToDelete.isEmpty()) {
            return new ResponseEntity<>("Deletion failed", HttpStatus.OK);
        }
        else {
            countryRepository.delete(countryToDelete.get());
            return new ResponseEntity<>("Deletion successful", HttpStatus.OK);
        }
    }

    @PostMapping("/deletecountries")
    public ResponseEntity<Object> deleteCountries(@Valid @RequestBody List<countries> countries) {
        countryRepository.deleteAll(countries);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
