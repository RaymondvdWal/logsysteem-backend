package com.example.logsysteem.services;

import com.example.logsysteem.dtos.WorkStationDto;
import com.example.logsysteem.exceptions.ActionNotPossibleException;
import com.example.logsysteem.exceptions.RecordNotFoundException;
import com.example.logsysteem.model.Malfunction;
import com.example.logsysteem.model.Operation;
import com.example.logsysteem.model.User;
import com.example.logsysteem.model.WorkStation;
import com.example.logsysteem.repository.UserRepository;
import com.example.logsysteem.repository.WorkstationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkStationService {

    private final WorkstationRepository workstationRepository;
    private final UserRepository userRepository;

    public WorkStationService(WorkstationRepository workstationRepository, UserRepository userRepository) {
        this.workstationRepository = workstationRepository;
        this.userRepository = userRepository;
    }

    List<WorkStationDto> workStationDtoList = new ArrayList<>();

    public List<WorkStationDto> getAllWorkStations() throws RecordNotFoundException {

        List<WorkStation> workStations = workstationRepository.findAll();

        if (workStations.isEmpty()) {
            throw new RecordNotFoundException("No workstations found");
        }

        for (WorkStation workstation: workStations) {
            workStationDtoList.add(transferModelToDTO(workstation));
        }

        return workStationDtoList;
    }


    public WorkStationDto getWorkStation(Long id) throws RecordNotFoundException{

        Optional<WorkStation> optionalWorkStation = workstationRepository.findById(id);

        if (optionalWorkStation.isEmpty()) {
            throw new RecordNotFoundException("Workstation with id:"+ id +" not found");
        }

        WorkStation workStation = optionalWorkStation.get();
        WorkStationDto workStationDto = transferModelToDTO(workStation);

        if (!workStation.getUsers().isEmpty()) {
            int lastUserIndex = workStation.getUsers().size() - 1;
            workStationDto.setUser(workStation.getUsers().get(lastUserIndex));
        }


        return workStationDto;
    }


    public WorkStationDto createWorkStation(WorkStationDto workStationDto) {
        WorkStation workStation = transferDtoToModel(workStationDto);

        workStation = workstationRepository.save(workStation);

        return transferModelToDTO(workStation);
    }


    public void updateWorkstation(Long id, @RequestBody WorkStationDto workStationDto ) throws RecordNotFoundException {

        Optional<WorkStation> optionalWorkStation = workstationRepository.findById(id);

        if (optionalWorkStation.isEmpty()) {
            throw new RecordNotFoundException("Workstation with id:"+ id +" not found");
        }

        WorkStation workStation = optionalWorkStation.get();

        workStation.setName(workStationDto.getName());
        workStation.setLocation(workStationDto.getLocation());
        workStation.setGeneralMessage(workStationDto.getGeneralMessage());
        workStation.setPushMessage(workStationDto.getPushMessage());

        workstationRepository.save(workStation);

    }


    public void deleteWorkStation(Long id) throws RecordNotFoundException {

        WorkStation workStation = workstationRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundException("WorkStation with id:"+ id +" not found")
        );

        List<Malfunction> malfunctionsAssignedToWorkstation = workStation.getMalfunctions();
        List<Operation> operationsAssignedToWorkstation = workStation.getOperations();
        List<User> usersAssignedToWorkStations = workStation.getUsers();

        if (!malfunctionsAssignedToWorkstation.isEmpty() || !operationsAssignedToWorkstation.isEmpty() || !usersAssignedToWorkStations.isEmpty()){
            throw new ActionNotPossibleException("Cannot delete a workstation which have operations, malfunctions or users assigned");
        }

        workstationRepository.delete(workStation);
    }

    public String assignUserToWorkStation(Long id, String username) throws  RecordNotFoundException {
        Optional<WorkStation> optionalWorkStation = workstationRepository.findById(id);
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalWorkStation.isEmpty() || optionalUser.isEmpty()) {
            throw new RecordNotFoundException("WorkStation or user not found");
        }

        WorkStation workStation = optionalWorkStation.get();
        User user = optionalUser.get();
        List<User> users = workStation.getUsers();
        users.add(user);
        workStation.setUsers(users);

        workstationRepository.save(workStation);

        return "User " + user.getUsername() + " is now assigned to workstation " + workStation.getName();
    }

    public List<String> usersAssignedToWorkstation(Long id) throws RecordNotFoundException {
        Optional<WorkStation> optionalWorkStation = workstationRepository.findById(id);

        if (optionalWorkStation.isEmpty()) {
            throw new RecordNotFoundException("Workstation not found");
        }

        WorkStation workStation = optionalWorkStation.get();
        List<User> usersAssignedToWorkstation = workStation.getUsers();
        List<String> usersList = new ArrayList<>();
        for (User user: usersAssignedToWorkstation) {
            usersList.add(user.getUsername());
        }

        return usersList;
    }

    public WorkStationDto transferModelToDTO(WorkStation workStation) {
        WorkStationDto workStationDto = new WorkStationDto();
        BeanUtils.copyProperties(workStation, workStationDto);
        return workStationDto;
    }

    public WorkStation transferDtoToModel(WorkStationDto workStationDto) {
        WorkStation workStation = new WorkStation();
        BeanUtils.copyProperties(workStationDto, workStation);
        return workStation;
    }

}
