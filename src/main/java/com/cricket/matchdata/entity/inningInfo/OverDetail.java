package com.cricket.matchdata.entity.inningInfo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(exclude = {"inning"})
public class OverDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "over_number")
    @JsonProperty("over")
    private int overNumber;

    @OneToMany(mappedBy = "overDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> deliveries = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "inning_id")
    @JsonBackReference
    private Inning inning;
}