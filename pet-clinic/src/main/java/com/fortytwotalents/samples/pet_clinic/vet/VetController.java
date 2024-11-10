package com.fortytwotalents.samples.pet_clinic.vet;

import com.fortytwotalents.samples.pet_clinic.specialty.Specialty;
import com.fortytwotalents.samples.pet_clinic.specialty.SpecialtyRepository;
import com.fortytwotalents.samples.pet_clinic.util.CustomCollectors;
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
@RequestMapping("/vets")
public class VetController {

    private final VetService vetService;
    private final SpecialtyRepository specialtyRepository;

    public VetController(final VetService vetService,
            final SpecialtyRepository specialtyRepository) {
        this.vetService = vetService;
        this.specialtyRepository = specialtyRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("vetSpecialtySpecialtiesValues", specialtyRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Specialty::getId, Specialty::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("vets", vetService.findAll());
        return "vet/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("vet") final VetDTO vetDTO) {
        return "vet/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("vet") @Valid final VetDTO vetDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "vet/add";
        }
        vetService.create(vetDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("vet.create.success"));
        return "redirect:/vets";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Integer id, final Model model) {
        model.addAttribute("vet", vetService.get(id));
        return "vet/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Integer id,
            @ModelAttribute("vet") @Valid final VetDTO vetDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "vet/edit";
        }
        vetService.update(id, vetDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("vet.update.success"));
        return "redirect:/vets";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Integer id,
            final RedirectAttributes redirectAttributes) {
        vetService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("vet.delete.success"));
        return "redirect:/vets";
    }

}
