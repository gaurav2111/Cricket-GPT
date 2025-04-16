package com.cricket.matchdata.entity.matchinfo;

import com.cricket.matchdata.deserializer.RegistryDeserializer;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@JsonDeserialize(using = RegistryDeserializer.class) // <-- Important
public class Registry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "registry", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Person> people = new ArrayList<>();
}