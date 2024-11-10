package com.fortytwotalents.samples.pet_clinic.pet;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PetDTO {

    private Integer id;

    @Size(max = 30)
    private String name;

    private LocalDate birthDate;

    private Integer owner;

    @NotNull
    private Integer type;

}
