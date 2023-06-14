package com.codegeek.hogwartsartifactsonline.wizard;

import com.codegeek.hogwartsartifactsonline.artifact.Artifact;
import com.codegeek.hogwartsartifactsonline.artifact.ArtifactRepository;
import com.codegeek.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() {

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Khadija");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Khalifa");

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("palloms");

        wizards = new ArrayList<>();

        wizards.addAll(List.of(w1, w2, w3));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        // Given
        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Abdulaziz");

        given(wizardRepository.findById(1)).willReturn(Optional.of(w));

        // When
        Wizard foundWizard = wizardService.findById(1);

        // Then
        assertThat(foundWizard.getName()).isEqualTo(w.getName());
        assertThat(foundWizard.getId()).isEqualTo(w.getId());
        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> {
            wizardService.findById(2);
        });

        // Then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with Id 2 :(");

        verify(wizardRepository, times(1)).findById(2);
    }



    @Test
    void testFindAll() {
        // Given
        given(wizardRepository.findAll()).willReturn(wizards);

        // When
        List<Wizard> foundWizards = wizardService.findAll();

        // Then
        assertThat(foundWizards.size()).isEqualTo(wizards.size());
        verify(wizardRepository, times(1)).findAll();
    }

    @Test
    void testSave() {
        // Given
        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Khalifa");

        given(wizardRepository.save(w)).willReturn(w);

        // When
        Wizard wizard = wizardService.save(w);

        // Then
        assertThat(wizard.getName()).isEqualTo(w.getName());
        assertThat(wizard.getId()).isEqualTo(w.getId());
        verify(wizardRepository, times(1)).save(w);

    }

    @Test
    void testUpdateSuccess() {
        // Given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("Khalifa");

        Wizard update = new Wizard();
        update.setId(1);
        update.setName("Abdullahi Abdulmalik");

        given(wizardRepository.findById(1)).willReturn(Optional.of(oldWizard));
        given(wizardRepository.save(Mockito.any(Wizard.class))).willReturn(oldWizard);

        // When
        Wizard updated = wizardService.update(1, update);

        // Then
        assertThat(updated.getName()).isEqualTo(update.getName());
        verify(wizardRepository, times(1)).findById(1);
        verify(wizardRepository, times(1)).save(oldWizard);
    }

    @Test
    void testUpdateNotFound() {
        Wizard update = new Wizard();
        update.setId(1);
        update.setName("Abdullahi Abdulmalik");

        given(wizardRepository.findById(1)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            wizardService.update(1, update);
        });

        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess() {
        Wizard toBeDeleted = new Wizard();
        toBeDeleted.setId(1);
        toBeDeleted.setName("Abdullahi Abdulmalik");

        given(wizardRepository.findById(1)).willReturn(Optional.of(toBeDeleted));
        doNothing().when(wizardRepository).deleteById(1);

        // when
        wizardService.delete(1);

        // Then
        verify(wizardRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        given(wizardRepository.findById(1)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> {
            wizardService.delete(1);
        });

        verify(wizardRepository, times(1)).findById(1);

    }

    @Test
    void testAssignArtifactSuccess() {
        // Given
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Khalifa");
        w2.addArtifact(artifact);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("palloms");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(artifact));
        given(wizardRepository.findById(3)).willReturn(Optional.of(w3));

        // When
        wizardService.assignArtifact(3, "1250808601744904192");

        // Then
        assertThat(artifact.getOwner().getId()).isEqualTo(3);
        assertThat(w3.getArtifacts()).contains(artifact);
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardID() {

        // Given
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Khalifa");
        w2.addArtifact(artifact);


        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(artifact));
        given(wizardRepository.findById(3)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            wizardService.assignArtifact(3, "1250808601744904192");
        });


        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with Id 3 :(");
        assertThat(artifact.getOwner().getId()).isEqualTo(2);

    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactID() {
        // Given
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            wizardService.assignArtifact(3, "1250808601744904192");
        });


        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with Id 1250808601744904192 :(");
    }

}