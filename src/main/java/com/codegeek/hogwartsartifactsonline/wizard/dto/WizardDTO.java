package com.codegeek.hogwartsartifactsonline.wizard.dto;

import jakarta.validation.constraints.NotEmpty;

public record WizardDTO(Integer id,
                        @NotEmpty(message = "Name cannot be empty")
                        String name,
                        Integer numberOfArtifacts) {

}
