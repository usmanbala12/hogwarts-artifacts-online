package com.codegeek.hogwartsartifactsonline.hogwartsuser;

import com.codegeek.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import com.codegeek.hogwartsartifactsonline.system.StatusCode;
import com.codegeek.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    List<HogwartsUser> users;

    @BeforeEach
    void setUp() {
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("sunusi");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("daneji");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");

        users = new ArrayList<>();

        users.addAll(List.of(u1,u2,u3));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAllUsers() throws Exception {
        // Given
        given(userService.findAll()).willReturn(users);

        // When and Then
        mockMvc.perform(get(baseUrl+"/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(users.size())));
    }

    @Test
    void findUserById() throws Exception {
        // Given
        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("sunusi");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");

        given(userService.findById(2)).willReturn(u2);

        // When and Then
        mockMvc.perform(get(baseUrl+"/users/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(u2.getId()))
                .andExpect(jsonPath("$.data.username").value(u2.getUsername()));

    }

    @Test
    void findUserByIdNotFound() throws Exception {
        // Given
        given(userService.findById(2)).willThrow(new ObjectNotFoundException("user", 2));

        // When and Then
        mockMvc.perform(get(baseUrl+"/users/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id 2 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void addUser() throws Exception {
        // Given
        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("daneji");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");

        String json = objectMapper.writeValueAsString(u3);

        given(userService.save(Mockito.any(HogwartsUser.class))).willReturn(u3);

        mockMvc.perform(post(baseUrl+"/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(u3.getId()))
                .andExpect(jsonPath("$.data.username").value(u3.getUsername()));


    }

    @Test
    void updateUser() throws Exception {
        // Given
        UserDto update = new UserDto(
                null,
                "daneji - update",
                true,
                "user"
        );

        String json = objectMapper.writeValueAsString(update);

        HogwartsUser updated = new HogwartsUser();
        updated.setId(3);
        updated.setUsername("daneji");
        updated.setPassword("qwerty");
        updated.setEnabled(false);
        updated.setRoles("user");

        given(userService.update(Mockito.any(Integer.class) ,Mockito.any(HogwartsUser.class))).willReturn(updated);

        mockMvc.perform(put(baseUrl+"/users/3").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(updated.getId()))
                .andExpect(jsonPath("$.data.username").value(updated.getUsername()));
    }

    @Test
    void updateUserNotFound() throws Exception {
        // Given
        UserDto update = new UserDto(
                null,
                "daneji - update",
                true,
                "user"
        );

        String json = objectMapper.writeValueAsString(update);

        given(userService.update(ArgumentMatchers.eq(1) ,Mockito.any(HogwartsUser.class))).willThrow(new ObjectNotFoundException("user", 1));

        mockMvc.perform(put(baseUrl+"/users/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void deleteUser() throws Exception {
        // Given
        doNothing().when(userService).delete(1);

        mockMvc.perform(delete(baseUrl+"/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(userService, times(1)).delete(1);
    }

    @Test
    void deleteUserNotFound() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("user", 1)).when(userService).delete(1);

        mockMvc.perform(delete(baseUrl+"/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(userService, times(1)).delete(1);
    }
}