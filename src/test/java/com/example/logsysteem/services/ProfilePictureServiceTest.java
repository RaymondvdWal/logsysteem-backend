package com.example.logsysteem.services;

import com.example.logsysteem.dtos.ProfilePictureDto;
import com.example.logsysteem.exceptions.RecordNotFoundException;
import com.example.logsysteem.model.ProfilePicture;
import com.example.logsysteem.repository.ProfilePictureRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfilePictureServiceTest {

    @InjectMocks
    private ProfilePictureService profilePictureService;

    @Mock
    private ProfilePictureRepository profilePictureRepository;

    @Captor
    ArgumentCaptor<ProfilePicture> captor;

    static ProfilePicture profilePicture;
    static ProfilePictureDto profilePictureDto;
    static byte[] testByte;
    static MockMultipartFile mockMultipartFile;

    @BeforeAll
    static void beforeAll() {
        profilePicture = new ProfilePicture();
        profilePicture.setId(1L);
        profilePicture.setFilename("test");
        profilePicture.setProfilePicture(testByte);
        profilePictureDto = new ProfilePictureDto();
        profilePictureDto.setFilename("dto test");
        profilePictureDto.setId(2L);
        profilePictureDto.setProfilePicture(profilePicture.getProfilePicture());
        mockMultipartFile = new MockMultipartFile("test",profilePicture.getProfilePicture());
    }

    @Test
    void getProfilePicture() {
        when(profilePictureRepository.findById(1L)).thenReturn(Optional.of(profilePicture));
        ProfilePictureDto profilePictureDto = profilePictureService.getProfilePicture(1L);
        assert(profilePictureDto.getFilename().equals("test"));
        assert(profilePictureDto.getId().equals(1L));
        assert(Arrays.equals(profilePictureDto.getProfilePicture(), testByte));
    }

    @Test
    void getProfilePictureException() {
        when(profilePictureRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> profilePictureService.getProfilePicture(any()));
    }

    @Test
    void uploadProfilePicture() throws IOException {
        when(profilePictureRepository.save(any())).thenReturn(profilePicture);
        profilePictureService.uploadProfilePicture(mockMultipartFile);
        verify(profilePictureRepository, times(1)).save(captor.capture());
        ProfilePicture picture = captor.getValue();
        assertEquals(profilePicture.getId(), 1L);
        assertEquals(profilePicture.getFilename(), picture.getFilename());
        assertEquals(mockMultipartFile.getBytes(), picture.getProfilePicture());
    }

    @Test
    void uploadProfilePictureRecordNotFoundException() {
        assertThrows(RecordNotFoundException.class, () -> profilePictureService.uploadProfilePicture(null));
    }

    @Test
    void updateProfilePicture() throws IOException {
        assertEquals("test", profilePicture.getFilename());
        when(profilePictureRepository.findById(any())).thenReturn(Optional.of(profilePicture));
        profilePictureService.updateProfilePicture(1L,mockMultipartFile);
        verify(profilePictureRepository, times(1)).save(captor.capture());
        ProfilePicture picture = captor.getValue();
        assertEquals("test",picture.getFilename());
        assertEquals(mockMultipartFile.getBytes(),picture.getProfilePicture());
    }

    @Test
    void updateProfilePictureRecordNotFoundException() {
        when(profilePictureRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> profilePictureService.updateProfilePicture(anyLong(),mockMultipartFile));
    }

    @Test
    void deleteProfilePicture() {
        when(profilePictureRepository.findById(anyLong())).thenReturn(Optional.of(profilePicture));
        profilePictureService.deleteProfilePicture(anyLong());
        verify(profilePictureRepository, times(1)).delete(profilePicture);
    }

    @Test
    void deleteProfilePictureRecordNotFoundException() {
        when(profilePictureRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> profilePictureService.deleteProfilePicture(anyLong()));
    }

    @Test
    void transferModelToDTO() {
        ProfilePictureDto profilePictureDto1 = profilePictureService.transferModelToDTO(profilePicture);
        assertEquals(profilePictureDto1.getFilename(),profilePicture.getFilename());
    }
}