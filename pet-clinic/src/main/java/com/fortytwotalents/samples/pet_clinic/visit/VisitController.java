package com.fortytwotalents.samples.pet_clinic.visit;

import com.fortytwotalents.samples.pet_clinic.pet.Pet;
import com.fortytwotalents.samples.pet_clinic.pet.PetRepository;
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
@RequestMapping("/visits")
public class VisitController {

    private final VisitService visitService;
    private final PetRepository petRepository;

    public VisitController(final VisitService visitService, final PetRepository petRepository) {
        this.visitService = visitService;
        this.petRepository = petRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("petValues", petRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Pet::getId, Pet::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("visits", visitService.findAll());
        return "visit/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("visit") final VisitDTO visitDTO) {
        return "visit/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("visit") @Valid final VisitDTO visitDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "visit/add";
        }
        visitService.create(visitDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("visit.create.success"));
        return "redirect:/visits";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Integer id, final Model model) {
        model.addAttribute("visit", visitService.get(id));
        return "visit/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Integer id,
            @ModelAttribute("visit") @Valid final VisitDTO visitDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "visit/edit";
        }
        visitService.update(id, visitDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("visit.update.success"));
        return "redirect:/visits";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Integer id,
            final RedirectAttributes redirectAttributes) {
        visitService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("visit.delete.success"));
        return "redirect:/visits";
    }

}
