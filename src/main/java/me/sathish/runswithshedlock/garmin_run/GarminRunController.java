package me.sathish.runswithshedlock.garmin_run;

import jakarta.validation.Valid;
import me.sathish.runswithshedlock.runner.Runner;
import me.sathish.runswithshedlock.runner.RunnerRepository;
import me.sathish.runswithshedlock.util.CustomCollectors;
import me.sathish.runswithshedlock.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/garminRuns")
public class GarminRunController {

    private final GarminRunService garminRunService;
    private final RunnerRepository runnerRepository;

    public GarminRunController(final GarminRunService garminRunService,
            final RunnerRepository runnerRepository) {
        this.garminRunService = garminRunService;
        this.runnerRepository = runnerRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("runnerValues", runnerRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Runner::getId, Runner::getUsername)));
    }

    @GetMapping
    public String list(@RequestParam(name = "filter", required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final Page<GarminRunDTO> garminRuns = garminRunService.findAll(filter, pageable);
        model.addAttribute("garminRuns", garminRuns);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(garminRuns));
        return "garminRun/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("garminRun") final GarminRunDTO garminRunDTO) {
        return "garminRun/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("garminRun") @Valid final GarminRunDTO garminRunDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "garminRun/add";
        }
        garminRunService.create(garminRunDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("garminRun.create.success"));
        return "redirect:/garminRuns";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("garminRun", garminRunService.get(id));
        return "garminRun/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("garminRun") @Valid final GarminRunDTO garminRunDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "garminRun/edit";
        }
        garminRunService.update(id, garminRunDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("garminRun.update.success"));
        return "redirect:/garminRuns";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        garminRunService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("garminRun.delete.success"));
        return "redirect:/garminRuns";
    }

}
