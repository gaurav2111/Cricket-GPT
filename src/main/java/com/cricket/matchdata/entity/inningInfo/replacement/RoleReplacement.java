package com.cricket.matchdata.entity.inningInfo.replacement;


import com.cricket.matchdata.entity.inningInfo.Delivery;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RoleReplacement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`in`")
    private String inPlayer;

    @Column(name = "`out`")
    private String outPlayer;

    private String reason;
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @JsonProperty("in")
    public void setInPlayer(String inPlayer) {
        this.inPlayer = inPlayer;
    }

    @JsonProperty("out")
    public void setOutPlayer(String outPlayer) {
        this.outPlayer = outPlayer;
    }

    // Getters and setters
}