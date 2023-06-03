package com.example.backend.controllers;


import com.example.backend.models.paintings;
import com.example.backend.repositories.PaintingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class PaintingsController {

    @Autowired
    PaintingRepository museumRepository;

    @PostMapping("/paints")
    public ResponseEntity<Object> createMuseum(@RequestBody paintings paint)
            throws Exception {
        try {
            paintings nc = museumRepository.save(paint);
            System.out.println(nc.name);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        } catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("museums.name_UNIQUE"))
                error = "useralreadyexists";
            else
                error = "undefinederror";
            Map<String, String>
                    map = new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/paints")
    public Page<paintings> getAllPaints(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return museumRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @PostMapping("/deletepaints")
    public ResponseEntity<HttpStatus> deletePaints(@RequestBody List<paintings> paints) {
        List<Long> listOfIds = new ArrayList<>();
        for (paintings artist: paints){
            listOfIds.add(artist.id);
        }
        museumRepository.deleteAllById(listOfIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/paints/{id}")
    public ResponseEntity<paintings> updatePaints(@PathVariable(value = "id") Long museumId,
                                               @RequestBody paintings museum) {
        paintings mus = null;
        Optional<paintings> cc = museumRepository.findById(museumId);
        if (cc.isPresent()) {
            mus = cc.get();
            mus.name = museum.name;
            museumRepository.save(mus);
            return ResponseEntity.ok(mus);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found");
        }
    }

}
