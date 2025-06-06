package me.sathish.runswithshedlock.run_event;

import jakarta.validation.Valid;
import me.sathish.runswithshedlock.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/runEvents")
public class RunEventController {

    private final RunEventService runEventService;

    public RunEventController(final RunEventService runEventService) {
        this.runEventService = runEventService;
    }

    @GetMapping
    public String list(@RequestParam(name = "filter", required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final Page<RunEventDTO> runEvents = runEventService.findAll(filter, pageable);
        model.addAttribute("runEvents", runEvents);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(runEvents));
        return "runEvent/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("runEvent") final RunEventDTO runEventDTO) {
        return "runEvent/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("runEvent") @Valid final RunEventDTO runEventDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "runEvent/add";
        }
        runEventService.create(runEventDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("runEvent.create.success"));
        return "redirect:/runEvents";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("runEvent", runEventService.get(id));
        return "runEvent/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("runEvent") @Valid final RunEventDTO runEventDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "runEvent/edit";
        }
        runEventService.update(id, runEventDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("runEvent.update.success"));
        return "redirect:/runEvents";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        runEventService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("runEvent.delete.success"));
        return "redirect:/runEvents";
    }

}
