package com.fortytwotalents.samples.pet_clinic.owner;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OwnerDTO {

    private Integer id;

    @Size(max = 30)
    private String firstName;

    @Size(max = 30)
    private String lastName;

    @Size(max = 255)
    private String address;

    @Size(max = 80)
    private String city;

    @Size(max = 20)
    private String telephone;

}
