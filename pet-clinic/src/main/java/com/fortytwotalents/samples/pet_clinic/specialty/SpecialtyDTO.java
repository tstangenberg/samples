package com.fortytwotalents.samples.pet_clinic.specialty;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialtyDTO {

  private Integer id;

  @Size(max = 80)
  private String name;
}
