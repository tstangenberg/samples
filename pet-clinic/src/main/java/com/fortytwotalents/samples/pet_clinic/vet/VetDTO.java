package com.fortytwotalents.samples.pet_clinic.vet;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VetDTO {

  private Integer id;

  @Size(max = 30)
  private String firstName;

  @Size(max = 30)
  private String lastName;

  private List<Integer> vetSpecialtySpecialties;
}
