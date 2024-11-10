package com.fortytwotalents.samples.pet_clinic.type;

import com.fortytwotalents.samples.pet_clinic.pet.Pet;
import com.fortytwotalents.samples.pet_clinic.pet.PetRepository;
import com.fortytwotalents.samples.pet_clinic.util.NotFoundException;
import com.fortytwotalents.samples.pet_clinic.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TypeService {

    private final TypeRepository typeRepository;
    private final PetRepository petRepository;

    public TypeService(final TypeRepository typeRepository, final PetRepository petRepository) {
        this.typeRepository = typeRepository;
        this.petRepository = petRepository;
    }

    public List<TypeDTO> findAll() {
        final List<Type> types = typeRepository.findAll(Sort.by("id"));
        return types.stream()
                .map(type -> mapToDTO(type, new TypeDTO()))
                .toList();
    }

    public TypeDTO get(final Integer id) {
        return typeRepository.findById(id)
                .map(type -> mapToDTO(type, new TypeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TypeDTO typeDTO) {
        final Type type = new Type();
        mapToEntity(typeDTO, type);
        return typeRepository.save(type).getId();
    }

    public void update(final Integer id, final TypeDTO typeDTO) {
        final Type type = typeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(typeDTO, type);
        typeRepository.save(type);
    }

    public void delete(final Integer id) {
        typeRepository.deleteById(id);
    }

    private TypeDTO mapToDTO(final Type type, final TypeDTO typeDTO) {
        typeDTO.setId(type.getId());
        typeDTO.setName(type.getName());
        return typeDTO;
    }

    private Type mapToEntity(final TypeDTO typeDTO, final Type type) {
        type.setName(typeDTO.getName());
        return type;
    }

    public ReferencedWarning getReferencedWarning(final Integer id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Type type = typeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Pet typePet = petRepository.findFirstByType(type);
        if (typePet != null) {
            referencedWarning.setKey("type.pet.type.referenced");
            referencedWarning.addParam(typePet.getId());
            return referencedWarning;
        }
        return null;
    }

}
