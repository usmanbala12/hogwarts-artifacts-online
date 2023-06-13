package com.codegeek.hogwartsartifactsonline.artifact.converter;

import com.codegeek.hogwartsartifactsonline.artifact.Artifact;
import com.codegeek.hogwartsartifactsonline.artifact.dto.ArtifactDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactDtoToArtifactConverter implements Converter<ArtifactDTO, Artifact> {
    @Override
    public Artifact convert(ArtifactDTO source) {
        Artifact artifact = new Artifact();
        artifact.setId(source.id());
        artifact.setName(source.name());
        artifact.setDescription(source.description());
        artifact.setImageUrl(source.imageUrl());
        return artifact;
    }
}
