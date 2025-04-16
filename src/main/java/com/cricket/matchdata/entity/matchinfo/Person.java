package com.cricket.matchdata.entity.matchinfo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // key of the map
    private String uuid; // value of the map

    @ManyToOne
    @JoinColumn(name = "registry_id")
    @JsonBackReference
    private Registry registry;
}