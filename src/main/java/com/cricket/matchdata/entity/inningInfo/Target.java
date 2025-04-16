package com.cricket.matchdata.entity.inningInfo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(exclude = {"inning"})

public class Target {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int overs;
    private int runs;

    @OneToOne
    @JoinColumn(name = "inning_id")
    @JsonBackReference
    private Inning inning;
}