package com.example.backend.models;

import jakarta.persistence.*;


@Entity
@Table(name = "paintings")
@Access(AccessType.FIELD)
public class paintings {

    public paintings(){}
    public paintings(Long id){
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    public Long id;
    
}

