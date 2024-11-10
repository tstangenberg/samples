package com.fortytwotalents.samples.pet_clinic.pet;

import com.fortytwotalents.samples.pet_clinic.owner.Owner;
import com.fortytwotalents.samples.pet_clinic.owner.OwnerRepository;
import com.fortytwotalents.samples.pet_clinic.type.Type;
import com.fortytwotalents.samples.pet_clinic.type.TypeRepository;
import com.fortytwotalents.samples.pet_clinic.util.CustomCollectors;
import com.fortytwotalents.samples.pet_clinic.util.ReferencedWarning;
import com.fortytwotalents.samples.pet_clinic.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;
    private final OwnerRepository ownerRepository;
    private final TypeRepository typeRepository;

    public PetController(final PetService petService, final OwnerRepository ownerRepository,
            final TypeRepository typeRepository) {
        this.petService = petService;
        this.ownerRepository = ownerRepository;
        this.typeRepository = typeRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("ownerValues", ownerRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Owner::getId, Owner::getId)));
        model.addAttribute("typeValues", typeRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Type::getId, Type::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("pets", petService.findAll());
        return "pet/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("pet") final PetDTO petDTO) {
        return "pet/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("pet") @Valid final PetDTO petDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "pet/add";
        }
        petService.create(petDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("pet.create.success"));
        return "redirect:/pets";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Integer id, final Model model) {
        model.addAttribute("pet", petService.get(id));
        return "pet/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Integer id,
            @ModelAttribute("pet") @Valid final PetDTO petDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "pet/edit";
        }
        petService.update(id, petDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("pet.update.success"));
        return "redirect:/pets";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Integer id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = petService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            petService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("pet.delete.success"));
        }
        return "redirect:/pets";
    }

}
