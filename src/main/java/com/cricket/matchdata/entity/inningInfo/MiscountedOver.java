package com.cricket.matchdata.entity.inningInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MiscountedOver {
    private Integer balls;
    private String umpire;
}