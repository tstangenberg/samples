package com.fortytwotalents.samples.pet_clinic.pet;

import com.fortytwotalents.samples.pet_clinic.owner.Owner;
import com.fortytwotalents.samples.pet_clinic.type.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Integer> {

  Pet findFirstByOwner(Owner owner);

  Pet findFirstByType(Type type);
}
