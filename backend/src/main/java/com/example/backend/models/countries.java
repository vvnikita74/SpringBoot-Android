package com.example.backend.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "countries")
@Access(AccessType.FIELD)
public class countries {

    public countries() { }
    public countries(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    @Column(name = "name", nullable = false, unique = true)
    @NotNull(message = "name must be not null")
    public String name;

    public String getName() {
        return this.name;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "country")
    public List<artists> artists = new ArrayList<>();

}