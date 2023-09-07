package com.example.logsysteem.controllers;

import com.example.logsysteem.dtos.ProfilePictureDto;
import com.example.logsysteem.filter.JwtRequestFilter;
import com.example.logsysteem.model.ProfilePicture;
import com.example.logsysteem.services.ProfilePictureService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(WorkStationController.class)
@ExtendWith(SpringExtension.class)
class ProfilePictureControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProfilePictureService profilePictureService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private WorkStationService workStationService;

    static ProfilePicture profilePicture;
    static ProfilePictureDto profilePictureDto;
    static byte[] testByte;
    static MockMultipartFile mockMultipartFile;

    @BeforeAll
    static void beforeAll() throws IOException {
        testByte = new byte[256];
        profilePicture = new ProfilePicture();
        profilePicture.setId(1L);
        profilePicture.setFilename("test");
        profilePicture.setProfilePicture(testByte);
        profilePictureDto = new ProfilePictureDto();
        profilePictureDto.setFilename("dto test");
        profilePictureDto.setId(2L);
        profilePictureDto.setProfilePicture(profilePicture.getProfilePicture());
        mockMultipartFile = new MockMultipartFile(
                "test",
                "Hello, World!".getBytes());
    }

   /* @Test
    void uploadProfilePicture() throws Exception{
        mvc.perform(multipart("/pic/new")
                .file(mockMultipartFile))
                .andExpect(status().isCreated())
                .andExpect(content().string("Successfully uploaded your profile picture"));



    }*/

    @Test
    void getProfilePicture() {
    }

    @Test
    void changeProfilePicture() {
    }

    @Test
    void deleteProfilePicture() {
    }

    public static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}