package com.example.logsysteem.services;

import com.example.logsysteem.dtos.WorkStationDto;
import com.example.logsysteem.enums.Location;
import com.example.logsysteem.exceptions.ActionNotPossibleException;
import com.example.logsysteem.exceptions.RecordNotFoundException;
import com.example.logsysteem.model.ProfilePicture;
import com.example.logsysteem.model.User;
import com.example.logsysteem.model.WorkStation;
import com.example.logsysteem.repository.UserRepository;
import com.example.logsysteem.repository.WorkstationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkStationServiceTest {

    @InjectMocks
    private WorkStationService workStationService;

    @Mock
    private WorkstationRepository workstationRepository;

    @Mock
    private UserRepository userRepository;

    static WorkStation workStation;
    static User user;
    static WorkStationDto workStationDto;
    static List<WorkStation> workStations = new ArrayList<>();
    static List<User> users = new ArrayList<>();
    static List<String> usersAssignedTest = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        users.add(new User());
        users.add(new User());
        users.add(new User("Laatste", "$2a$10$tPSVd8E9qpOm1NX3k4mOye/Yz9vqkOvvxneKVq26q9jq305NF6Noy", "Dikke", "Doei", true, "123456", "MH@novi.nl", new HashSet<>(), new ArrayList<>(),new ProfilePicture()));
        workStation = new WorkStation(1L, "Cobas", "ff", "ss", Location.UTRECHT, new ArrayList<>(), Collections.emptyList(), Collections.emptyList());
        workStations.add(workStation);
        workStation = new WorkStation(2L, "Sysmex", "kk", "jj", Location.WOERDEN, users, Collections.emptyList(), Collections.emptyList());
        workStations.add(workStation);
        workStation = new WorkStation(3L, "Lamp", "dd", "ee", Location.NIEUWEGEIN, new ArrayList<>(), Collections.emptyList(), Collections.emptyList());
        workStations.add(workStation);
        workStationDto = new WorkStationDto();
        workStationDto.setId(1L);
        workStationDto.setGeneralMessage("ss");
        workStationDto.setLocation(Location.UTRECHT);
        workStationDto.setPushMessage("ff");
        workStationDto.setName("Cobas");
    }

    @Test
    void getAllWorkStations() {
    //given
        when(workstationRepository.findAll()).thenReturn(workStations);
    // when
        List<WorkStationDto> allWorkStations = workStationService.getAllWorkStations();
    // then
        assertEquals(allWorkStations.size(),3);
    }

    @Test
    void getAllWorkStationsException() {
    //given
        when(workstationRepository.findAll()).thenReturn(Collections.emptyList());
    // when
        assertThrows(RecordNotFoundException.class, () -> workStationService.getAllWorkStations());
    }

    @Test
    void getWorkStationWithUser() {
        //given
        when(workstationRepository.findById(2L)).thenReturn(Optional.ofNullable(workStations.get(1)));
        // when
        WorkStationDto workStationDto = workStationService.getWorkStation(2L);
        // then
        assertEquals(workStationDto.getId(),2L);
        assertEquals(workStationDto.getUser().getUsername(),"Laatste");
    }

    @Test
    void getWorkStationWithoutUser() {
        //given
        when(workstationRepository.findById(1L)).thenReturn(Optional.ofNullable(workStations.get(0)));
        // when
        WorkStationDto workStationDto = workStationService.getWorkStation(1L);
        // then
        assertEquals(workStationDto.getId(),1L);
        assertNull(workStationDto.getUser());
    }

    @Test
    void getWorkStationException() {
        //given
        when(workstationRepository.findById(2L)).thenReturn(Optional.empty());
        // when
        assertThrows(RecordNotFoundException.class, () -> workStationService.getWorkStation(2L));
    }

    @Test
    void createWorkStation() {
        //given
        when(workstationRepository.save(any())).thenReturn(workStations.get(0));
        //when
        WorkStationDto workStationDtoResponse = workStationService.createWorkStation(workStationDto);
        //then
        verify(workstationRepository, times(1)).save(any());
        assertEquals("Cobas", workStationDtoResponse.getName());
    }

    @Test
    void updateWorkstation() {
        assertEquals("Lamp", workStations.get(2).getName());
        when(workstationRepository.findById(any())).thenReturn(Optional.ofNullable(workStations.get(2)));
        workStationService.updateWorkstation(3L,workStationDto);
        verify(workstationRepository, times(1)).save(any());
        assertEquals("Cobas", workStations.get(2).getName());
    }

    @Test
    void updateWorkstationException() {
        when(workstationRepository.findById(any())).thenReturn(Optional.empty());
        verify(workstationRepository, times(0)).save(any());
        assertThrows(RecordNotFoundException.class, () -> workStationService.updateWorkstation(3L,workStationDto));
    }

    @Test
    void deleteWorkStation() {
        when(workstationRepository.findById(any())).thenReturn(Optional.ofNullable(workStations.get(0)));
        workStationService.deleteWorkStation(1L);
        verify(workstationRepository, times(1)).delete(workStations.get(0));
    }

    @Test
    void deleteWorkStationRecordNotFoundException() {
        when(workstationRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> workStationService.deleteWorkStation(any()));
    }

    @Test
    void deleteWorkStationActionNotPossibleException() {
        when(workstationRepository.findById(2L)).thenReturn(Optional.of(workStations.get(1)));
        assertThrows(ActionNotPossibleException.class, () -> workStationService.deleteWorkStation(2L));
    }

    @Test
    void assignUserToWorkStation() {
        when(workstationRepository.findById(any())).thenReturn(Optional.of(workStations.get(1)));
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(users.get(2)));
        workStationService.assignUserToWorkStation(1L,"Laatste");
        verify(workstationRepository, times(1)).save(any());
        assertEquals("Laatste", workStations.get(1).getUsers().get(2).getUsername());
    }

    @Test
    void assignUserToWorkStationsException() {
        when(workstationRepository.findById(any())).thenReturn(Optional.empty());
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> workStationService.assignUserToWorkStation(1L,"Laatste"));
    }

    @Test
    void usersAssignedToWorkstation() {
        when(workstationRepository.findById(any())).thenReturn(Optional.of(workStations.get(1)));
        workStationService.usersAssignedToWorkstation(2L);
        assertEquals(3, workStations.get(1).getUsers().size());
        assertEquals("Laatste", workStations.get(1).getUsers().get(2).getUsername());
    }

    @Test
    void usersAssignedToWorkStationsException() {
        when(workstationRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> workStationService.usersAssignedToWorkstation(1L));
    }

    @Test
    void transferModelToDTO() {
        WorkStationDto workStationDto1 = workStationService.transferModelToDTO(workStations.get(0));
        assertEquals(workStationDto1.getName(),  workStations.get(0).getName());
    }

    @Test
    void transferDtoToModel() {
        WorkStation workStation1 = workStationService.transferDtoToModel(workStationDto);
        assertEquals(workStation1.getName(), workStationDto.getName());
    }
}