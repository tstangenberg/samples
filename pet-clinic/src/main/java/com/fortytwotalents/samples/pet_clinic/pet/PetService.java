package com.fortytwotalents.samples.pet_clinic.pet;

import com.fortytwotalents.samples.pet_clinic.owner.Owner;
import com.fortytwotalents.samples.pet_clinic.owner.OwnerRepository;
import com.fortytwotalents.samples.pet_clinic.type.Type;
import com.fortytwotalents.samples.pet_clinic.type.TypeRepository;
import com.fortytwotalents.samples.pet_clinic.util.NotFoundException;
import com.fortytwotalents.samples.pet_clinic.util.ReferencedWarning;
import com.fortytwotalents.samples.pet_clinic.visit.Visit;
import com.fortytwotalents.samples.pet_clinic.visit.VisitRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PetService {

  private final PetRepository petRepository;
  private final OwnerRepository ownerRepository;
  private final TypeRepository typeRepository;
  private final VisitRepository visitRepository;

  public PetService(
      final PetRepository petRepository,
      final OwnerRepository ownerRepository,
      final TypeRepository typeRepository,
      final VisitRepository visitRepository) {
    this.petRepository = petRepository;
    this.ownerRepository = ownerRepository;
    this.typeRepository = typeRepository;
    this.visitRepository = visitRepository;
  }

  public List<PetDTO> findAll() {
    final List<Pet> pets = petRepository.findAll(Sort.by("id"));
    return pets.stream().map(pet -> mapToDTO(pet, new PetDTO())).toList();
  }

  public PetDTO get(final Integer id) {
    return petRepository
        .findById(id)
        .map(pet -> mapToDTO(pet, new PetDTO()))
        .orElseThrow(NotFoundException::new);
  }

  public Integer create(final PetDTO petDTO) {
    final Pet pet = new Pet();
    mapToEntity(petDTO, pet);
    return petRepository.save(pet).getId();
  }

  public void update(final Integer id, final PetDTO petDTO) {
    final Pet pet = petRepository.findById(id).orElseThrow(NotFoundException::new);
    mapToEntity(petDTO, pet);
    petRepository.save(pet);
  }

  public void delete(final Integer id) {
    petRepository.deleteById(id);
  }

  private PetDTO mapToDTO(final Pet pet, final PetDTO petDTO) {
    petDTO.setId(pet.getId());
    petDTO.setName(pet.getName());
    petDTO.setBirthDate(pet.getBirthDate());
    petDTO.setOwner(pet.getOwner() == null ? null : pet.getOwner().getId());
    petDTO.setType(pet.getType() == null ? null : pet.getType().getId());
    return petDTO;
  }

  private Pet mapToEntity(final PetDTO petDTO, final Pet pet) {
    pet.setName(petDTO.getName());
    pet.setBirthDate(petDTO.getBirthDate());
    final Owner owner =
        petDTO.getOwner() == null
            ? null
            : ownerRepository
                .findById(petDTO.getOwner())
                .orElseThrow(() -> new NotFoundException("owner not found"));
    pet.setOwner(owner);
    final Type type =
        petDTO.getType() == null
            ? null
            : typeRepository
                .findById(petDTO.getType())
                .orElseThrow(() -> new NotFoundException("type not found"));
    pet.setType(type);
    return pet;
  }

  public ReferencedWarning getReferencedWarning(final Integer id) {
    final ReferencedWarning referencedWarning = new ReferencedWarning();
    final Pet pet = petRepository.findById(id).orElseThrow(NotFoundException::new);
    final Visit petVisit = visitRepository.findFirstByPet(pet);
    if (petVisit != null) {
      referencedWarning.setKey("pet.visit.pet.referenced");
      referencedWarning.addParam(petVisit.getId());
      return referencedWarning;
    }
    return null;
  }
}
