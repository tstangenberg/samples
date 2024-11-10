package com.fortytwotalents.samples.pet_clinic.type;

import com.fortytwotalents.samples.pet_clinic.util.ReferencedWarning;
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
@RequestMapping("/types")
public class TypeController {

    private final TypeService typeService;

    public TypeController(final TypeService typeService) {
        this.typeService = typeService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("types", typeService.findAll());
        return "type/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("type") final TypeDTO typeDTO) {
        return "type/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("type") @Valid final TypeDTO typeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "type/add";
        }
        typeService.create(typeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("type.create.success"));
        return "redirect:/types";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Integer id, final Model model) {
        model.addAttribute("type", typeService.get(id));
        return "type/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Integer id,
            @ModelAttribute("type") @Valid final TypeDTO typeDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "type/edit";
        }
        typeService.update(id, typeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("type.update.success"));
        return "redirect:/types";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Integer id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = typeService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            typeService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("type.delete.success"));
        }
        return "redirect:/types";
    }

}
