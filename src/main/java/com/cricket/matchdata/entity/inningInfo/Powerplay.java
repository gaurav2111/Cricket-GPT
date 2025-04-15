package com.cricket.matchdata.entity.inningInfo;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
public class Powerplay {
    private double from;
    private double to;
    private String type;
}