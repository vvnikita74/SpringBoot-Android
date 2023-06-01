package com.example.backend.controllers;

import com.example.backend.models.artists;
import com.example.backend.repositories.ArtistRepository;
import com.example.backend.repositories.CountryRepository;
import com.example.backend.tools.DataValidationException;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ArtistsController {
    
    private final ArtistRepository artistRepository;

    public ArtistsController(ArtistRepository artistRepository, CountryRepository countryRepository) {
        this.artistRepository = artistRepository;
    }

    @GetMapping("/artists")
    public List<artists> getAllArtists() {
        return (List<artists>) artistRepository.findAll();
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<Object> getArtist(@PathVariable(value="id") Long artistId) throws DataValidationException {
        artists artist = artistRepository.findById(artistId)
        .orElseThrow(() -> new DataValidationException("Артист с таким индексом не найдена"));
    return ResponseEntity.ok(artist);
    }

    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(@Valid @RequestBody artists artist) throws DataValidationException {
        try {
            artists nc = artistRepository.save(artist);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex) {
            if (ex.getMessage().contains("artist.name_UNIQUE"))
                throw new DataValidationException("Этот артист уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @PutMapping("/artists/{id}")
    public ResponseEntity<Object> updateArtist(@PathVariable(value = "id") Long artistId, @RequestBody artists artistDetails) throws DataValidationException {
        try {
            artists artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new DataValidationException("Страна с таким индексом не найдена"));
            artist.name = artistDetails.name;
            artist.age = artistDetails.age;
            artistRepository.save(artist);
            return ResponseEntity.ok(artist);
        }
        catch (Exception ex) {
            if (ex.getMessage().contains("artist.name_UNIQUE"))
                throw new DataValidationException("Этот артист уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @DeleteMapping("/artists/{id}")
    public ResponseEntity<String> deleteArtist(@PathVariable(value = "id") Long artistId) {
        Optional<artists> artistToDelete  = artistRepository.findById(artistId);
        if (artistToDelete.isEmpty()) {
            return new ResponseEntity<>("Deletion failed", HttpStatus.OK);
        }
        else {
            artistRepository.delete(artistToDelete.get());
            return new ResponseEntity<>("Deletion successful", HttpStatus.OK);
        }
    }

    @PostMapping("/deleteartists")
    public ResponseEntity<Object> deleteArtists(@Valid @RequestBody List<artists> artists) {
        artistRepository.deleteAll(artists);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
