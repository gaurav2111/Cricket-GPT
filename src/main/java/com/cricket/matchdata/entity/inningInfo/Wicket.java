package com.cricket.matchdata.entity.inningInfo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Wicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kind;

    @JsonProperty("player_out")
    private String playerOut;

    @OneToMany(mappedBy = "wicket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fielder> fielders = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    @JsonBackReference
    private Delivery delivery;
}