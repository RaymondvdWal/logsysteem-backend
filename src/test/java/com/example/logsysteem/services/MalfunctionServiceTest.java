package com.example.logsysteem.services;

import com.example.logsysteem.dtos.MalfunctionDto;
import com.example.logsysteem.enums.Location;
import com.example.logsysteem.exceptions.RecordNotFoundException;
import com.example.logsysteem.model.Malfunction;
import com.example.logsysteem.model.WorkStation;
import com.example.logsysteem.repository.MalfunctionRepository;
import com.example.logsysteem.repository.WorkstationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MalfunctionServiceTest {

    @InjectMocks
    private MalfunctionService malfunctionService;

    @Mock
    private MalfunctionRepository malfunctionRepository;

    @Mock
    private WorkstationRepository workstationRepository;

    static Malfunction malfunction;
    static MalfunctionDto malfunctionDto;
    static Authentication authentication;
    static List<Malfunction> malfunctions = new ArrayList<>();

    @BeforeAll
    static void beforeAll(){
        malfunction = new Malfunction();
        malfunction.setId(1L);
        malfunctions.add(malfunction);
        malfunction = new Malfunction();
        malfunction.setId(2L);
        malfunction.setDescription("bijna stuk");
        malfunctions.add(malfunction);
        malfunction = new Malfunction();
        malfunction.setId(3L);
        malfunctions.add(malfunction);
        malfunctionDto = new MalfunctionDto();
        malfunctionDto.setId(1L);
        malfunctionDto.setDescription("helemaal stuk");
        authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "test";
            }
        };
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void getAllMalfunctions() {
        when(malfunctionRepository.findAll()).thenReturn(malfunctions);
        List<MalfunctionDto> allMalfunctions = malfunctionService.getAllMalfunctions();
        assertEquals(allMalfunctions.size(), 3);
    }

    @Test
    void getAllMalfunctionsException() {
        when(malfunctionRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(RecordNotFoundException.class, () -> malfunctionService.getAllMalfunctions());
    }

    @Test
    void getMalfunction() {
        when(malfunctionRepository.findById(any())).thenReturn(Optional.ofNullable(malfunctions.get(0)));
        MalfunctionDto malfunctionDto = malfunctionService.getMalfunction(1L);
        assertEquals(malfunctionDto.getId(),1L);
    }

    @Test
    void getMalfunctionException() {
        when(malfunctionRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> malfunctionService.getMalfunction(any()));
    }

    @Test
    void createMalfunction() {
        when(malfunctionRepository.save(any())).thenReturn(malfunctions.get(0));
        MalfunctionDto malfunctionDtoResponse = malfunctionService.createMalfunction(malfunctionDto);
        verify(malfunctionRepository, times(1)).save(any());
        assertEquals(1L,malfunctionDtoResponse.getId());
        assertEquals("test",authentication.getName());
    }

    @Test
    void updateMalfunction() {
        assertEquals("bijna stuk", malfunctions.get(1).getDescription());
        when(malfunctionRepository.findById(any())).thenReturn(Optional.ofNullable(malfunctions.get(1)));
        malfunctionService.updateMalfunction(2L,malfunctionDto);
        verify(malfunctionRepository, times(1)).save(any());
        assertEquals("helemaal stuk", malfunctions.get(1).getDescription());
        assertEquals("test", authentication.getName());
    }

    @Test
    void updateMalfunctionException() {
        when(malfunctionRepository.findById(any())).thenReturn(Optional.empty());
        verify(malfunctionRepository, times(0)).save(any());
        assertThrows(RecordNotFoundException.class, () -> malfunctionService.updateMalfunction(2L,malfunctionDto));
    }

    @Test
    void deleteMalfunction() {
        when(malfunctionRepository.findById(any())).thenReturn(Optional.ofNullable(malfunctions.get(0)));
        malfunctionService.deleteMalfunction(malfunctions.get(0).getId());
        verify(malfunctionRepository, times(1)).delete(malfunctions.get(0));
    }

    @Test
    void deleteMalfunctionException() {
        when(malfunctionRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> malfunctionService.deleteMalfunction(any()));
    }

    @Test
    void assignMalfunctionToWorkStation() {
        when(malfunctionRepository.findById(any())).thenReturn(Optional.ofNullable(malfunctions.get(0)));
        when(workstationRepository.findById(any())).thenReturn(Optional.of(new WorkStation(1L, "Sysmax", "Tekst", "Werkhof", Location.UTRECHT, Collections.emptyList(), Collections.emptyList(), Collections.emptyList())));
        malfunctionService.assignMalfunctionToWorkStation(malfunctions.get(0).getId(),1L);
        verify(malfunctionRepository, times(1)).save(any());
        assertEquals("Sysmax", malfunctions.get(0).getWorkStation().getName());
    }

    @Test
    void assignMalfunctionToWorkStationsException() {
        when(malfunctionRepository.findById(any())).thenReturn(Optional.empty());
        verify(malfunctionRepository, times(0)).save(any());
        assertThrows(RecordNotFoundException.class, () -> malfunctionService.assignMalfunctionToWorkStation(1L, 1L));
    }

    @Test
    void transferModelToDTO() {
        MalfunctionDto malfunctionDto1 = malfunctionService.transferModelToDTO(malfunctions.get(0));
        assertEquals(malfunctionDto1.getDescription(), malfunctions.get(0).getDescription());
    }

    @Test
    void transferDtoToModel() {
        Malfunction malfunction1 = malfunctionService.transferDtoToModel(malfunctionDto);
        assertEquals(malfunction1.getDescription(), malfunctionDto.getDescription());
    }
}