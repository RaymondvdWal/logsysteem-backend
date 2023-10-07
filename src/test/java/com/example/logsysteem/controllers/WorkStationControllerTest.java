package com.example.logsysteem.controllers;

import com.example.logsysteem.dtos.WorkStationDto;
import com.example.logsysteem.enums.Location;
import com.example.logsysteem.model.User;
import com.example.logsysteem.model.WorkStation;
import com.example.logsysteem.repository.UserRepository;
import com.example.logsysteem.repository.WorkstationRepository;
import com.example.logsysteem.services.WorkStationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class WorkStationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WorkStationService workStationService;

    @Autowired
    private WorkstationRepository workstationRepository;

    @Autowired
    private UserRepository userRepository;

    private WorkStationDto workStationDto1;

    @BeforeEach
     void setUp() {
        User henk = new User("Laatste", "$2a$10$tPSVd8E9qpOm1NX3k4mOye/Yz9vqkOvvxneKVq26q9jq305NF6Noy", "Dikke", "Doei", true, "123456", "MH@novi.nl", new HashSet<>(),null,null);
        userRepository.save(henk);
        WorkStation workStation = new WorkStation(1L, "Cobas", "ff", "ss", Location.UTRECHT,null, null, null);
        workstationRepository.save(workStation);
        workStation = new WorkStation(2L, "Sysmex", "kk", "jj", Location.WOERDEN, null, null, null);
        workstationRepository.save(workStation);
        workStation = new WorkStation(3L, "Lamp", "dd", "ee", Location.NIEUWEGEIN, null, null, null);
        workstationRepository.save(workStation);
        workStationDto1 = new WorkStationDto();
        workStationDto1.setId(1L);
        workStationDto1.setGeneralMessage("ss");
        workStationDto1.setLocation(Location.UTRECHT);
        workStationDto1.setPushMessage("ff");
        workStationDto1.setName("Cobas");
    }

    @Test
    void getAllWorkStations() throws Exception {
        List<WorkStationDto> allWorkStations = workStationService.getAllWorkStations();

        mvc.perform(get("/workstation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(allWorkStations.size()));
    }

    @Test
    void getWorkStations() throws Exception {
        WorkStationDto workStationDto = workStationService.getWorkStation(1L);

        mvc.perform(get("/workstation/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(workStationDto.id))
                .andExpect(jsonPath("generalMessage").value(workStationDto.generalMessage))
                .andExpect(jsonPath("location").value(workStationDto.location.name()))
                .andExpect(jsonPath("pushMessage").value(workStationDto.pushMessage))
                .andExpect(jsonPath("name").value(workStationDto.name));
    }

    @Test
    void createWorkStations() throws Exception {
        mvc.perform(post("/workstation/new").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(workStationDto1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("generalMessage").value(workStationDto1.generalMessage))
                .andExpect(jsonPath("location").value(workStationDto1.location.name()))
                .andExpect(jsonPath("pushMessage").value(workStationDto1.pushMessage))
                .andExpect(jsonPath("name").value(workStationDto1.name));
    }

    @Test
    void updateWorkStations() throws Exception {
        mvc.perform(put("/workstation/2").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(workStationDto1)))
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(workStationDto1)));
    }

    @Test
    void deleteWorkStations() throws Exception {
        mvc.perform(delete("/workstation/1"))
                .andExpect(status().isOk());
    }

    @Test
    void assignUserToWorkStation() throws Exception {
        mvc.perform(put("/workstation/2/user/Laatste")
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