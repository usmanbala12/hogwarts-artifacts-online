package com.codegeek.hogwartsartifactsonline.artifact;

import com.codegeek.hogwartsartifactsonline.artifact.converter.ArtifactDtoToArtifactConverter;
import com.codegeek.hogwartsartifactsonline.artifact.converter.ArtifactToArtifactDtoConverter;
import com.codegeek.hogwartsartifactsonline.artifact.dto.ArtifactDTO;
import com.codegeek.hogwartsartifactsonline.system.Result;
import com.codegeek.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/artifacts")
public class ArtifactController {

    private final ArtifactService artifactService;

    private final ArtifactToArtifactDtoConverter artifactDtoConverter;

    private final ArtifactDtoToArtifactConverter artifactConverter;

    public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDtoConverter artifactDtoConverter, ArtifactDtoToArtifactConverter artifactConverter) {
        this.artifactService = artifactService;
        this.artifactDtoConverter = artifactDtoConverter;
        this.artifactConverter = artifactConverter;
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId){
        Artifact foundArtifact = this.artifactService.findById(artifactId);
        ArtifactDTO artifactDTO = artifactDtoConverter.convert(foundArtifact);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", artifactDTO);
    }

    @GetMapping
    public Result findAllArtifacts() {
        List<Artifact> artifacts = artifactService.findAll();
        // convert found artifact to a list artifact DTOs
        List<ArtifactDTO> artifactDTOS = artifacts.stream().map(this.artifactDtoConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", artifactDTOS);
    }

    @PostMapping
    public Result addArtifact(@Valid @RequestBody ArtifactDTO artifactDTO) {
        Artifact artifact = artifactConverter.convert(artifactDTO);
        Artifact savedArtifact = artifactService.save(artifact);
        ArtifactDTO savedArtifactDto = artifactDtoConverter.convert(savedArtifact);

        return new Result(true, StatusCode.SUCCESS, "Add Success", savedArtifactDto);
    }

    @PutMapping("/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId, @Valid @RequestBody ArtifactDTO artifactDTO) {
        Artifact artifact = artifactConverter.convert(artifactDTO);
        Artifact updatedArtifact = artifactService.update(artifactId, artifact);
        ArtifactDTO updatedArtifactDto = artifactDtoConverter.convert(updatedArtifact);
        return new Result(true,StatusCode.SUCCESS, "Artifact update success", updatedArtifactDto);
    }

    @DeleteMapping("{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId) {
        artifactService.delete(artifactId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
