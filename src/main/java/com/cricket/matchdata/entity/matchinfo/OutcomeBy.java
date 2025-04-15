package com.cricket.matchdata.entity.matchinfo;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutcomeBy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer runs;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer wickets;
}