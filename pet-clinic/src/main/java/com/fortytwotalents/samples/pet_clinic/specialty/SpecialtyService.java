package com.fortytwotalents.samples.pet_clinic.specialty;

import com.fortytwotalents.samples.pet_clinic.util.NotFoundException;
import com.fortytwotalents.samples.pet_clinic.vet.VetRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SpecialtyService {

  private final SpecialtyRepository specialtyRepository;
  private final VetRepository vetRepository;

  public SpecialtyService(
      final SpecialtyRepository specialtyRepository, final VetRepository vetRepository) {
    this.specialtyRepository = specialtyRepository;
    this.vetRepository = vetRepository;
  }

  public List<SpecialtyDTO> findAll() {
    final List<Specialty> specialties = specialtyRepository.findAll(Sort.by("id"));
    return specialties.stream().map(specialty -> mapToDTO(specialty, new SpecialtyDTO())).toList();
  }

  public SpecialtyDTO get(final Integer id) {
    return specialtyRepository
        .findById(id)
        .map(specialty -> mapToDTO(specialty, new SpecialtyDTO()))
        .orElseThrow(NotFoundException::new);
  }

  public Integer create(final SpecialtyDTO specialtyDTO) {
    final Specialty specialty = new Specialty();
    mapToEntity(specialtyDTO, specialty);
    return specialtyRepository.save(specialty).getId();
  }

  public void update(final Integer id, final SpecialtyDTO specialtyDTO) {
    final Specialty specialty =
        specialtyRepository.findById(id).orElseThrow(NotFoundException::new);
    mapToEntity(specialtyDTO, specialty);
    specialtyRepository.save(specialty);
  }

  public void delete(final Integer id) {
    final Specialty specialty =
        specialtyRepository.findById(id).orElseThrow(NotFoundException::new);
    // remove many-to-many relations at owning side
    vetRepository
        .findAllByVetSpecialtySpecialties(specialty)
        .forEach(vet -> vet.getVetSpecialtySpecialties().remove(specialty));
    specialtyRepository.delete(specialty);
  }

  private SpecialtyDTO mapToDTO(final Specialty specialty, final SpecialtyDTO specialtyDTO) {
    specialtyDTO.setId(specialty.getId());
    specialtyDTO.setName(specialty.getName());
    return specialtyDTO;
  }

  private Specialty mapToEntity(final SpecialtyDTO specialtyDTO, final Specialty specialty) {
    specialty.setName(specialtyDTO.getName());
    return specialty;
  }
}
