package com.cricket.matchdata.entity.inningInfo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Fielder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean substitute;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "wicket_id")
    private Wicket wicket;
}