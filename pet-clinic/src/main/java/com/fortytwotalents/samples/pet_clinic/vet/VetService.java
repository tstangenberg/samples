package com.fortytwotalents.samples.pet_clinic.vet;

import com.fortytwotalents.samples.pet_clinic.specialty.Specialty;
import com.fortytwotalents.samples.pet_clinic.specialty.SpecialtyRepository;
import com.fortytwotalents.samples.pet_clinic.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class VetService {

    private final VetRepository vetRepository;
    private final SpecialtyRepository specialtyRepository;

    public VetService(final VetRepository vetRepository,
            final SpecialtyRepository specialtyRepository) {
        this.vetRepository = vetRepository;
        this.specialtyRepository = specialtyRepository;
    }

    public List<VetDTO> findAll() {
        final List<Vet> vets = vetRepository.findAll(Sort.by("id"));
        return vets.stream()
                .map(vet -> mapToDTO(vet, new VetDTO()))
                .toList();
    }

    public VetDTO get(final Integer id) {
        return vetRepository.findById(id)
                .map(vet -> mapToDTO(vet, new VetDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final VetDTO vetDTO) {
        final Vet vet = new Vet();
        mapToEntity(vetDTO, vet);
        return vetRepository.save(vet).getId();
    }

    public void update(final Integer id, final VetDTO vetDTO) {
        final Vet vet = vetRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(vetDTO, vet);
        vetRepository.save(vet);
    }

    public void delete(final Integer id) {
        vetRepository.deleteById(id);
    }

    private VetDTO mapToDTO(final Vet vet, final VetDTO vetDTO) {
        vetDTO.setId(vet.getId());
        vetDTO.setFirstName(vet.getFirstName());
        vetDTO.setLastName(vet.getLastName());
        vetDTO.setVetSpecialtySpecialties(vet.getVetSpecialtySpecialties().stream()
                .map(specialty -> specialty.getId())
                .toList());
        return vetDTO;
    }

    private Vet mapToEntity(final VetDTO vetDTO, final Vet vet) {
        vet.setFirstName(vetDTO.getFirstName());
        vet.setLastName(vetDTO.getLastName());
        final List<Specialty> vetSpecialtySpecialties = specialtyRepository.findAllById(
                vetDTO.getVetSpecialtySpecialties() == null ? Collections.emptyList() : vetDTO.getVetSpecialtySpecialties());
        if (vetSpecialtySpecialties.size() != (vetDTO.getVetSpecialtySpecialties() == null ? 0 : vetDTO.getVetSpecialtySpecialties().size())) {
            throw new NotFoundException("one of vetSpecialtySpecialties not found");
        }
        vet.setVetSpecialtySpecialties(new HashSet<>(vetSpecialtySpecialties));
        return vet;
    }

}
