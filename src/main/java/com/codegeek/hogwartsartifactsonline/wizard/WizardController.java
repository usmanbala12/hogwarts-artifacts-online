package com.codegeek.hogwartsartifactsonline.wizard;

import com.codegeek.hogwartsartifactsonline.system.Result;
import com.codegeek.hogwartsartifactsonline.system.StatusCode;
import com.codegeek.hogwartsartifactsonline.wizard.converter.WizardDtoToWizardConverter;
import com.codegeek.hogwartsartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import com.codegeek.hogwartsartifactsonline.wizard.dto.WizardDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {

    private final WizardService wizardService;
    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;
    private final WizardDtoToWizardConverter wizardDtoToWizardConverter;

    public WizardController(WizardService wizardService, WizardToWizardDtoConverter wizardToWizardDtoConverter, WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId) {
        Wizard foundWizard = wizardService.findById(wizardId);
        WizardDTO wizardDTO = wizardToWizardDtoConverter.convert(foundWizard);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", wizardDTO);
    }

    @GetMapping
    public Result findAllWizards() {
        List<Wizard> foundWizards = wizardService.findAll();
        List<WizardDTO> foundWizardDTOs = foundWizards.stream()
                .map(wizardToWizardDtoConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", foundWizardDTOs);
    }

    @PostMapping
    public Result addWizard(@RequestBody @Valid WizardDTO wizardDTO) {
        Wizard wizard = wizardDtoToWizardConverter.convert(wizardDTO);
        Wizard savedWizard = wizardService.save(wizard);
        WizardDTO wizardDTO1 = wizardToWizardDtoConverter.convert(savedWizard);
        return new Result(true, StatusCode.SUCCESS, "Add Success", wizardDTO1);
    }

    @PutMapping("/{wizardId}")
    public Result updateWizard(@PathVariable Integer wizardId, @RequestBody @Valid WizardDTO wizardDTO) {
        Wizard update = wizardDtoToWizardConverter.convert(wizardDTO);
        Wizard updated = wizardService.update(wizardId, update);
        WizardDTO updatedDto = wizardToWizardDtoConverter.convert(updated);

        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedDto);
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteUpdate(@PathVariable Integer wizardId) {
        wizardService.delete(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

}
