package com.fortytwotalents.samples.pet_clinic.visit;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VisitDTO {

    private Integer id;

    private LocalDate visitDate;

    @Size(max = 255)
    private String description;

    private Integer pet;

}
