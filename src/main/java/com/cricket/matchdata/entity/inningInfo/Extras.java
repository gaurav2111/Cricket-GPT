package com.cricket.matchdata.entity.inningInfo;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Extras {
    private Integer wides;
    private Integer legbyes;
    private Integer noballs;
    private Integer byes;
    private Integer penalty;
}