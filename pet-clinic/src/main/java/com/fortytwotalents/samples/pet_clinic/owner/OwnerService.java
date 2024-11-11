package com.fortytwotalents.samples.pet_clinic.owner;

import com.fortytwotalents.samples.pet_clinic.pet.Pet;
import com.fortytwotalents.samples.pet_clinic.pet.PetRepository;
import com.fortytwotalents.samples.pet_clinic.util.NotFoundException;
import com.fortytwotalents.samples.pet_clinic.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

  private final OwnerRepository ownerRepository;
  private final PetRepository petRepository;

  public OwnerService(final OwnerRepository ownerRepository, final PetRepository petRepository) {
    this.ownerRepository = ownerRepository;
    this.petRepository = petRepository;
  }

  public List<OwnerDTO> findAll() {
    final List<Owner> owners = ownerRepository.findAll(Sort.by("id"));
    return owners.stream().map(owner -> mapToDTO(owner, new OwnerDTO())).toList();
  }

  public OwnerDTO get(final Integer id) {
    return ownerRepository
        .findById(id)
        .map(owner -> mapToDTO(owner, new OwnerDTO()))
        .orElseThrow(NotFoundException::new);
  }

  public Integer create(final OwnerDTO ownerDTO) {
    final Owner owner = new Owner();
    mapToEntity(ownerDTO, owner);
    return ownerRepository.save(owner).getId();
  }

  public void update(final Integer id, final OwnerDTO ownerDTO) {
    final Owner owner = ownerRepository.findById(id).orElseThrow(NotFoundException::new);
    mapToEntity(ownerDTO, owner);
    ownerRepository.save(owner);
  }

  public void delete(final Integer id) {
    ownerRepository.deleteById(id);
  }

  private OwnerDTO mapToDTO(final Owner owner, final OwnerDTO ownerDTO) {
    ownerDTO.setId(owner.getId());
    ownerDTO.setFirstName(owner.getFirstName());
    ownerDTO.setLastName(owner.getLastName());
    ownerDTO.setAddress(owner.getAddress());
    ownerDTO.setCity(owner.getCity());
    ownerDTO.setTelephone(owner.getTelephone());
    return ownerDTO;
  }

  private Owner mapToEntity(final OwnerDTO ownerDTO, final Owner owner) {
    owner.setFirstName(ownerDTO.getFirstName());
    owner.setLastName(ownerDTO.getLastName());
    owner.setAddress(ownerDTO.getAddress());
    owner.setCity(ownerDTO.getCity());
    owner.setTelephone(ownerDTO.getTelephone());
    return owner;
  }

  public ReferencedWarning getReferencedWarning(final Integer id) {
    final ReferencedWarning referencedWarning = new ReferencedWarning();
    final Owner owner = ownerRepository.findById(id).orElseThrow(NotFoundException::new);
    final Pet ownerPet = petRepository.findFirstByOwner(owner);
    if (ownerPet != null) {
      referencedWarning.setKey("owner.pet.owner.referenced");
      referencedWarning.addParam(ownerPet.getId());
      return referencedWarning;
    }
    return null;
  }
}
