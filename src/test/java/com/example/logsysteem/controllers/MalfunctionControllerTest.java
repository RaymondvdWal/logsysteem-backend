package com.example.logsysteem.controllers;

import com.example.logsysteem.dtos.MalfunctionDto;
import com.example.logsysteem.enums.Status;
import com.example.logsysteem.enums.Urgency;
import com.example.logsysteem.model.Malfunction;
import com.example.logsysteem.model.WorkStation;
import com.example.logsysteem.repository.MalfunctionRepository;
import com.example.logsysteem.repository.WorkstationRepository;
import com.example.logsysteem.services.MalfunctionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class MalfunctionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MalfunctionService malfunctionService;

    @Autowired
    private MalfunctionRepository malfunctionRepository;

    @Autowired
    private WorkstationRepository workstationRepository;

    private MalfunctionDto malfunctionDto1;

    @BeforeEach
     void setUp() {
        Malfunction malfunction = new Malfunction();
        malfunction.setId(1L);
        malfunction.setDescription("hij doet het niet");
        malfunctionRepository.save(malfunction);
        malfunction = new Malfunction();
        malfunction.setId(2L);
        malfunction.setDescription("bijna stuk");
        malfunctionRepository.save(malfunction);
        malfunction = new Malfunction();
        malfunction.setId(3L);
        malfunctionRepository.save(malfunction);
        malfunctionDto1 = new MalfunctionDto();
        malfunctionDto1.setId(4L);
        malfunctionDto1.setDescription("helemaal stuk");
        malfunctionDto1.setSolution("nieuwe knop installeren");
        malfunctionDto1.setAction("rechts");
        malfunctionDto1.setStatus(Status.ONGEDAAN);
        malfunctionDto1.setUrgency(Urgency.HOOG);
        malfunctionDto1.setTitle("doen");

    }

    @Test
    void getAllMalfunctions() throws Exception{
        List<MalfunctionDto> allMalfunctions = malfunctionService.getAllMalfunctions();

        mvc.perform(get("/malfunction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(allMalfunctions.size()));


    }

    @Test
    void getMalfunction() throws Exception{
        MalfunctionDto malfunction1 = malfunctionService.getMalfunction(1L);

        mvc.perform(get("/malfunction/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(malfunction1.getId()))
                .andExpect(jsonPath("description").value(malfunction1.getDescription()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createMalfunction() throws Exception {
        mvc.perform(post("/malfunction/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(malfunctionDto1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("description").value(malfunctionDto1.getDescription()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateMalfunction() throws Exception {
        mvc.perform(put("/malfunction/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(malfunctionDto1)))
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(malfunctionDto1)));

    }

    @Test
    void deleteMalfunction() throws Exception {
        mvc.perform(delete("/malfunction/1"))
                .andExpect(status().isOk());
    }

    @Test
    void assignMalfunctionToWorkstation() throws Exception {
        WorkStation workStation = new WorkStation();
        workStation.setId(1L);
        workStation.setName("werkstation");
        workstationRepository.save(workStation);

        mvc.perform(put("/malfunction/3/workstation/1"))
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