package com.example.logsysteem.controllers;

import com.example.logsysteem.dtos.WorkStationDto;
import com.example.logsysteem.enums.Location;
import com.example.logsysteem.filter.JwtRequestFilter;
import com.example.logsysteem.model.ProfilePicture;
import com.example.logsysteem.model.User;
import com.example.logsysteem.model.WorkStation;
import com.example.logsysteem.services.WorkStationService;
import com.example.logsysteem.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(WorkStationController.class)
@ExtendWith(SpringExtension.class)
class WorkStationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WorkStationService workStationService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    static WorkStation workStation;
    static WorkStationDto workStationDto1;
    static WorkStationDto workStationDto2;
    static List<WorkStation> workStations = new ArrayList<>();
    static List<User> users = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        users.add(new User());
        users.add(new User());
        users.add(new User("Laatste", "$2a$10$tPSVd8E9qpOm1NX3k4mOye/Yz9vqkOvvxneKVq26q9jq305NF6Noy", "Dikke", "Doei", true, "123456", "MH@novi.nl", new HashSet<>(),Collections.emptyList(),new ProfilePicture()));
        workStation = new WorkStation(1L, "Cobas", "ff", "ss", Location.UTRECHT, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        workStations.add(workStation);
        workStation = new WorkStation(2L, "Sysmex", "kk", "jj", Location.WOERDEN, users, Collections.emptyList(), Collections.emptyList());
        workStations.add(workStation);
        workStation = new WorkStation(3L, "Lamp", "dd", "ee", Location.NIEUWEGEIN, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        workStations.add(workStation);
        workStationDto1 = new WorkStationDto();
        workStationDto1.setId(1L);
        workStationDto1.setGeneralMessage("ss");
        workStationDto1.setLocation(Location.UTRECHT);
        workStationDto1.setPushMessage("ff");
        workStationDto1.setName("Cobas");
        workStationDto2 = new WorkStationDto();
        workStationDto2.setId(2L);
        workStationDto2.setGeneralMessage("ss");
        workStationDto2.setLocation(Location.WOERDEN);
        workStationDto2.setPushMessage("ff");
        workStationDto2.setName("Sysmex");
    }

    @Test
    void getAllWorkStations() throws Exception {
        given(workStationService.getAllWorkStations()).willReturn(List.of(workStationDto1));

        mvc.perform(get("/workstation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].generalMessage").value("ss"))
                .andExpect(jsonPath("$[0].location").value(Location.UTRECHT.name()))
                .andExpect(jsonPath("$[0].pushMessage").value("ff"))
                .andExpect(jsonPath("$[0].name").value("Cobas"));
    }

    @Test
    void getWorkStations() throws Exception {
        given(workStationService.getWorkStation(any())).willReturn(workStationDto1);

        mvc.perform(get("/workstation/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("generalMessage").value("ss"))
                .andExpect(jsonPath("location").value(Location.UTRECHT.name()))
                .andExpect(jsonPath("pushMessage").value("ff"))
                .andExpect(jsonPath("name").value("Cobas"));
    }

    @Test
    void createWorkStations() throws Exception {
        given(workStationService.createWorkStation(any())).willReturn(workStationDto1);

        mvc.perform(post("/workstation/new").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(workStationDto1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("generalMessage").value("ss"))
                .andExpect(jsonPath("location").value(Location.UTRECHT.name()))
                .andExpect(jsonPath("pushMessage").value("ff"))
                .andExpect(jsonPath("name").value("Cobas"));
    }

    @Test
    void updateWorkStations() throws Exception {
        mvc.perform(put("/workstation/1").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(workStationDto2)))
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(workStationDto2)));
    }

    @Test
    void deleteWorkStations() throws Exception {
        mvc.perform(delete("/workstation/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Workstation successfully deleted"));
    }

    @Test
    void assignUserToWorkStation() throws Exception {
        mvc.perform(put("/workstation/1/user/Laatste")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(workStationDto1)))
                .andExpect(status().isOk());
    }

    @Test
    void getUsersAssignedToWorkstation() throws Exception {
        mvc.perform(get("/workstation/2/users"))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}