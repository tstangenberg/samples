package com.fortytwotalents.samples.pet_clinic.type;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeDTO {

  private Integer id;

  @Size(max = 80)
  private String name;
}
