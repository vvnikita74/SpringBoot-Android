package com.example.backend.models;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;


@Entity
@Table(name = "museums")
@Access(AccessType.FIELD)
public class museums {
    
    public museums(){}
    public museums(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "location")
    public String location;

    /*

    @JsonIgnore
    @OneToMany
    public List<paintings>
    paintings = new ArrayList<>(); 

    */

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "users_museums", joinColumns = @JoinColumn(name = "museum_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    public Set<users> users = new HashSet<>();

    public String getName(){
        return this.name;
    }
    
}
