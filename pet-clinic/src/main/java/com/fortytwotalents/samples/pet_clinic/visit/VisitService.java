package com.fortytwotalents.samples.pet_clinic.visit;

import com.fortytwotalents.samples.pet_clinic.pet.Pet;
import com.fortytwotalents.samples.pet_clinic.pet.PetRepository;
import com.fortytwotalents.samples.pet_clinic.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PetRepository petRepository;

    public VisitService(final VisitRepository visitRepository, final PetRepository petRepository) {
        this.visitRepository = visitRepository;
        this.petRepository = petRepository;
    }

    public List<VisitDTO> findAll() {
        final List<Visit> visits = visitRepository.findAll(Sort.by("id"));
        return visits.stream()
                .map(visit -> mapToDTO(visit, new VisitDTO()))
                .toList();
    }

    public VisitDTO get(final Integer id) {
        return visitRepository.findById(id)
                .map(visit -> mapToDTO(visit, new VisitDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final VisitDTO visitDTO) {
        final Visit visit = new Visit();
        mapToEntity(visitDTO, visit);
        return visitRepository.save(visit).getId();
    }

    public void update(final Integer id, final VisitDTO visitDTO) {
        final Visit visit = visitRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(visitDTO, visit);
        visitRepository.save(visit);
    }

    public void delete(final Integer id) {
        visitRepository.deleteById(id);
    }

    private VisitDTO mapToDTO(final Visit visit, final VisitDTO visitDTO) {
        visitDTO.setId(visit.getId());
        visitDTO.setVisitDate(visit.getVisitDate());
        visitDTO.setDescription(visit.getDescription());
        visitDTO.setPet(visit.getPet() == null ? null : visit.getPet().getId());
        return visitDTO;
    }

    private Visit mapToEntity(final VisitDTO visitDTO, final Visit visit) {
        visit.setVisitDate(visitDTO.getVisitDate());
        visit.setDescription(visitDTO.getDescription());
        final Pet pet = visitDTO.getPet() == null ? null : petRepository.findById(visitDTO.getPet())
                .orElseThrow(() -> new NotFoundException("pet not found"));
        visit.setPet(pet);
        return visit;
    }

}
