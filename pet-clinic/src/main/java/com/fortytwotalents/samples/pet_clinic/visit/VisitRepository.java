package com.fortytwotalents.samples.pet_clinic.visit;

import com.fortytwotalents.samples.pet_clinic.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VisitRepository extends JpaRepository<Visit, Integer> {

    Visit findFirstByPet(Pet pet);

}
