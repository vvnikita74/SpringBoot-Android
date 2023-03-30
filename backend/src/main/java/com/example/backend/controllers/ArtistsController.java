package com.example.backend.controllers;

import com.example.backend.models.artists;
import com.example.backend.models.countries;
import com.example.backend.repositories.ArtistRepository;
import com.example.backend.repositories.CountryRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class ArtistsController {
    
    private final ArtistRepository artistRepository;
    private final CountryRepository countryRepository;

    public ArtistsController(ArtistRepository artistRepository, CountryRepository countryRepository) {
        this.artistRepository = artistRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping("/artists")
    public List<artists> getAllArtists() {
        return (List<artists>) artistRepository.findAll();
    }

    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(@RequestBody artists artist) {

        // Проверка на пустое значение поля name  в запросе
        if (Objects.equals(artist.getName(), "")) {
            return new ResponseEntity<>("name must be not null", HttpStatus.BAD_REQUEST);
        }

        // Проверяем, существует ли уже запись с таким именем
        if (artistRepository.findByName(artist.getName()).isPresent()) {
            return new ResponseEntity<>("this artists already exists", HttpStatus.BAD_REQUEST);
        }

        // Проверка на наличие страны по id
        Optional<countries> artist_country = countryRepository.findById(artist.country.id);
        if (artist_country.isEmpty()) {
            return new ResponseEntity<>("this country for artist not exists", HttpStatus.BAD_REQUEST);
        }

        artist.country = artist_country.get();
        artists nc = artistRepository.save(artist);
        return new ResponseEntity<Object>(nc, HttpStatus.OK);
    }

}
