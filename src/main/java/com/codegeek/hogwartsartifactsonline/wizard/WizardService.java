package com.codegeek.hogwartsartifactsonline.wizard;

import com.codegeek.hogwartsartifactsonline.artifact.Artifact;
import com.codegeek.hogwartsartifactsonline.artifact.ArtifactRepository;
import com.codegeek.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {
    private final WizardRepository wizardRepository;

    private final ArtifactRepository artifactRepository;

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
    }

    public Wizard findById(Integer wizardId) {
        return wizardRepository.findById(wizardId).orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
    }

    public List<Wizard> findAll() {
        return wizardRepository.findAll();
    }

    public Wizard save(Wizard wizard) {
       return wizardRepository.save(wizard);
    }

    public Wizard update(Integer wizardId, Wizard update) {
        return wizardRepository.findById(wizardId).map(wizard -> {
            wizard.setName(update.getName());
            return wizardRepository.save(wizard);
        }).orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
    }

    public void delete(Integer wizardId) {
        Wizard wizardTobeDeleted = wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));

        wizardTobeDeleted.removeAllArtifacts();
        wizardRepository.deleteById(wizardId);
    }

    public void assignArtifact(Integer wizardId, String artifactId) {
        // find this artifact from db
        Artifact artifactToBeAssigned = artifactRepository.findById(artifactId).orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));

        // find this wizard from db
        Wizard wizard = wizardRepository.findById(wizardId).orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));

        // artifact assignment
        // we need to see if the artifact is already owned by some wizard
        if(artifactToBeAssigned.getOwner() != null) {
            artifactToBeAssigned.getOwner().removeArtifact(artifactToBeAssigned);
        }
        wizard.addArtifact(artifactToBeAssigned);
    }
}
