package com.codegeek.hogwartsartifactsonline.wizard;

import com.codegeek.hogwartsartifactsonline.system.StatusCode;
import com.codegeek.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.codegeek.hogwartsartifactsonline.wizard.dto.WizardDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
class WizardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    WizardService wizardService;

    @Value("${api.endpoint.base-url}")
    String baseUrl;


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
    void testFindWizardByIdSuccess() throws Exception {
        // Given
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Khadija");

        given(wizardService.findById(1)).willReturn(w1);

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.name").value(w1.getName()));
    }

    @Test
    void testFindWizardByIdNotFound() throws Exception {
        // Given
        given(wizardService.findById(1)).willThrow(new ObjectNotFoundException("wizard", 1));

        // When and Then
        mockMvc.perform(get(baseUrl+"/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllWizards() throws Exception {
        // Given
        given(wizardService.findAll()).willReturn(wizards);

        // When and Then
        mockMvc.perform(get(baseUrl+"/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(wizards.size())))
                .andExpect(jsonPath("$.data[0].id").value(wizards.get(0).getId()))
                .andExpect(jsonPath("$.data[0].name").value(wizards.get(0).getName()))
                .andExpect(jsonPath("$.data[1].id").value(wizards.get(1).getId()))
                .andExpect(jsonPath("$.data[1].name").value(wizards.get(1).getName()));
    }

    @Test
    void testAddWizard() throws Exception {
        // Given
        WizardDTO w = new WizardDTO(null, "khadija", null);

        String json = objectMapper.writeValueAsString(w);

        Wizard savedWizard = new Wizard();
        savedWizard.setId(2);
        savedWizard.setName("khadija");

        given(wizardService.save(Mockito.any(Wizard.class))).willReturn(savedWizard);

        // When and Then
        mockMvc.perform(post(baseUrl+"/wizards").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedWizard.getName()));
    }

    @Test
    void testUpdateWizardSuccess() throws Exception {
        // Given
        WizardDTO wizardDTO = new WizardDTO(1, "khadija", null);

        String json = objectMapper.writeValueAsString(wizardDTO);

        Wizard updatedWizard = new Wizard();
        updatedWizard.setId(2);
        updatedWizard.setName("khadija");

        given(wizardService.update(Mockito.any(Integer.class),Mockito.any(Wizard.class))).willReturn(updatedWizard);

        // When and Then
        mockMvc.perform(put(baseUrl+"/wizards/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.name").value(updatedWizard.getName()));
    }

    @Test
    void testUpdateWizardWithNonExistentId() throws Exception {
        WizardDTO wizardDTO = new WizardDTO(1, "khadija", null);

        String json = objectMapper.writeValueAsString(wizardDTO);

        given(wizardService.update(Mockito.any(Integer.class), Mockito.any(Wizard.class))).willThrow(new ObjectNotFoundException("wizard", 1));

        mockMvc.perform(put(baseUrl+"/wizards/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testDeleteWizardSuccess() throws Exception {
        // Given
        doNothing().when(wizardService).delete(1);

        mockMvc.perform(delete(baseUrl+"/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteWizardNotFound() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("wizard", 1)).when(wizardService).delete(1);

        // When and Then
        mockMvc.perform(delete(baseUrl+"/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with Id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}