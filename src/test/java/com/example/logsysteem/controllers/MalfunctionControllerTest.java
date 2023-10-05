package com.example.logsysteem.controllers;

import com.example.logsysteem.dtos.MalfunctionDto;
import com.example.logsysteem.enums.Status;
import com.example.logsysteem.enums.Urgency;
import com.example.logsysteem.filter.JwtRequestFilter;
import com.example.logsysteem.model.Malfunction;
import com.example.logsysteem.services.MalfunctionService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(MalfunctionController.class)
@ExtendWith(SpringExtension.class)
class MalfunctionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MalfunctionService malfunctionService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    static Malfunction malfunction;
    static MalfunctionDto malfunctionDto1;
    static MalfunctionDto malfunctionDto2;
    static List<Malfunction> malfunctions = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        malfunction = new Malfunction();
        malfunction.setId(1L);
        malfunction.setDescription("hij doet het niet");
        malfunctions.add(malfunction);
        malfunction = new Malfunction();
        malfunction.setId(2L);
        malfunction.setDescription("bijna stuk");
        malfunctions.add(malfunction);
        malfunction = new Malfunction();
        malfunction.setId(3L);
        malfunctions.add(malfunction);
        malfunctionDto1 = new MalfunctionDto();
        malfunctionDto1.setId(1L);
        malfunctionDto1.setDescription("helemaal stuk");
        malfunctionDto1.setSolution("nieuwe knop installeren");
        malfunctionDto1.setAction("rechts");
        malfunctionDto1.setStatus(Status.ONGEDAAN);
        malfunctionDto1.setUrgency(Urgency.HOOG);
        malfunctionDto1.setTitle("doen");
        malfunctionDto2 = new MalfunctionDto();
        malfunctionDto2.setId(2L);
        malfunctionDto2.setDescription("weer gemaakt");
        malfunctionDto2.setSolution("knopje links indrukken");
        malfunctionDto2.setAction("links");
        malfunctionDto2.setStatus(Status.BEZIG);
        malfunctionDto2.setUrgency(Urgency.LAAG);
        malfunctionDto2.setTitle("doen");
    }

    @Test
    void getAllMalfunctions() throws Exception{
        given(malfunctionService.getAllMalfunctions()).willReturn(List.of(malfunctionDto1));

        mvc.perform(get("/malfunction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].description").value("helemaal stuk"));
    }

    @Test
    void getMalfunction() throws Exception{
        given(malfunctionService.getMalfunction(any())).willReturn(malfunctionDto1);

        mvc.perform(get("/malfunction/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("description").value("helemaal stuk"));
    }

    @Test
    void createMalfunction() throws Exception {
        given(malfunctionService.createMalfunction(any())).willReturn(malfunctionDto1);

        mvc.perform(post("/malfunction/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(malfunctionDto1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("description").value("helemaal stuk"));
    }

    @Test
    void updateMalfunction() throws Exception {
        mvc.perform(put("/malfunction/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(malfunctionDto2)))
                .andExpect(status().isOk())
                .andExpect(content().string(asJsonString(malfunctionDto2)));

    }

    @Test
    void deleteMalfunction() throws Exception {
        mvc.perform(delete("/malfunction/1"))
                .andExpect(status().isOk());
    }

    @Test
    void assignMalfunctionToWorkstation() throws Exception {
        mvc.perform(put("/malfunction/1/workstation/1"))
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