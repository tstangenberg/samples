package com.fortytwotalents.samples.pet_clinic.specialty;

import com.fortytwotalents.samples.pet_clinic.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/specialties")
public class SpecialtyController {

  private final SpecialtyService specialtyService;

  public SpecialtyController(final SpecialtyService specialtyService) {
    this.specialtyService = specialtyService;
  }

  @GetMapping
  public String list(final Model model) {
    model.addAttribute("specialties", specialtyService.findAll());
    return "specialty/list";
  }

  @GetMapping("/add")
  public String add(@ModelAttribute("specialty") final SpecialtyDTO specialtyDTO) {
    return "specialty/add";
  }

  @PostMapping("/add")
  public String add(
      @ModelAttribute("specialty") @Valid final SpecialtyDTO specialtyDTO,
      final BindingResult bindingResult,
      final RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      return "specialty/add";
    }
    specialtyService.create(specialtyDTO);
    redirectAttributes.addFlashAttribute(
        WebUtils.MSG_SUCCESS, WebUtils.getMessage("specialty.create.success"));
    return "redirect:/specialties";
  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable(name = "id") final Integer id, final Model model) {
    model.addAttribute("specialty", specialtyService.get(id));
    return "specialty/edit";
  }

  @PostMapping("/edit/{id}")
  public String edit(
      @PathVariable(name = "id") final Integer id,
      @ModelAttribute("specialty") @Valid final SpecialtyDTO specialtyDTO,
      final BindingResult bindingResult,
      final RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      return "specialty/edit";
    }
    specialtyService.update(id, specialtyDTO);
    redirectAttributes.addFlashAttribute(
        WebUtils.MSG_SUCCESS, WebUtils.getMessage("specialty.update.success"));
    return "redirect:/specialties";
  }

  @PostMapping("/delete/{id}")
  public String delete(
      @PathVariable(name = "id") final Integer id, final RedirectAttributes redirectAttributes) {
    specialtyService.delete(id);
    redirectAttributes.addFlashAttribute(
        WebUtils.MSG_INFO, WebUtils.getMessage("specialty.delete.success"));
    return "redirect:/specialties";
  }
}
