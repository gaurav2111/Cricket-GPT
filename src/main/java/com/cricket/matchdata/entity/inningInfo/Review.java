package com.cricket.matchdata.entity.inningInfo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String batter;
    @Column(name = "`by`")
    private String by;
    private String decision;
    private String umpire;
    private String type;
    @JsonProperty("umpires_call")
    private Boolean umpiresCall;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    @JsonBackReference
    private Delivery delivery;
}