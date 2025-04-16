package com.cricket.matchdata.entity.inningInfo;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Powerplay {
    private double from;
    private double to;
    private String type;
}