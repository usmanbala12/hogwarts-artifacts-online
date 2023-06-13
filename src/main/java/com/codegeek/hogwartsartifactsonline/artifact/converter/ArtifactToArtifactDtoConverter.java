package com.codegeek.hogwartsartifactsonline.artifact.converter;

import com.codegeek.hogwartsartifactsonline.artifact.Artifact;
import com.codegeek.hogwartsartifactsonline.artifact.dto.ArtifactDTO;
import com.codegeek.hogwartsartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDTO> {

    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    public ArtifactToArtifactDtoConverter(WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }

    @Override
    public ArtifactDTO convert(Artifact source) {
        return new ArtifactDTO(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getImageUrl(),
                source.getOwner() != null ? wizardToWizardDtoConverter.convert(source.getOwner()) : null
        );
    }
}
