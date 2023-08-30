package com.example.logsysteem.services;

import com.example.logsysteem.dtos.OperationDto;
import com.example.logsysteem.enums.Status;
import com.example.logsysteem.exceptions.RecordNotFoundException;
import com.example.logsysteem.model.Operation;
import com.example.logsysteem.model.WorkStation;
import com.example.logsysteem.repository.OperationRepository;
import com.example.logsysteem.repository.WorkstationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OperationService {

    private final OperationRepository operationRepository;
    private final WorkstationRepository workStationRepository;

    public OperationService(OperationRepository operationRepository, WorkstationRepository workStationRepository) {
        this.operationRepository = operationRepository;
        this.workStationRepository = workStationRepository;
    }

    List<OperationDto> operationDtoList = new ArrayList<>();



    public List<OperationDto> getAllOperations() throws RecordNotFoundException {

        List<Operation> operations = operationRepository.findAll();

        if (operations.size() == 0) {
            throw new RecordNotFoundException("No operations found");
        }

        for (Operation operation : operations) {
            operationDtoList.add(transferModelToDTO(operation));
        }

        return operationDtoList;
    }


    public OperationDto getOperation(Long id) throws RecordNotFoundException{

        Optional<Operation> optionalOperation = operationRepository.findById(id);

        if (optionalOperation.isEmpty()) {
            throw new RecordNotFoundException("Operation with id:"+ id +" not found");
        }

        Operation operation = optionalOperation.get();

        return transferModelToDTO(operation);
    }


    public OperationDto createOperation(OperationDto operationDto) {
        Operation operation = transferDtoToModel(operationDto);

        operation = operationRepository.save(operation);

        return transferModelToDTO(operation);
    }


    public void updateOperation(Long id, @RequestBody OperationDto operationDto ) throws RecordNotFoundException {

        Operation operation = operationRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundException("Operation with id:"+ id +" not found")
        );

        operation.setId(id);
        operation.setDateIndication(operationDto.getDateIndication());
        operation.setInstruction(operationDto.getInstruction());
        operation.setComment(operationDto.getComment());
        operation.setStatus(operationDto.getStatus());
        operation.setName(operationDto.getName());
        operation.setDevice(operationDto.getDevice());

        if (operation.getStatus() == Status.KLAAR) {
            if (operationDto.getOperationDone() == null) {
                operation.setOperationDone(LocalDateTime.now());
            } else {
                operation.setOperationDone(operationDto.getOperationDone());
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                operation.setFinishedBy(authentication.getName());
            }
        }

        if (operation.getStatus() == Status.BEZIG) {
            if (operationDto.getOperationPickedUp() == null) {
                operation.setOperationPickedUp(LocalDateTime.now());
            } else {
                operation.setOperationPickedUp(operationDto.getOperationPickedUp());
            }
            operation.setFinishedBy(null);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                operation.setPickedUpBy(authentication.getName());
            }
        }

        if (operation.getStatus() == Status.ONGEDAAN) {
            operation.setOperationPickedUp(null);
            operation.setOperationDone(null);
        }

        operationRepository.save(operation);

    }


    public void deleteOperation(Long id) throws RecordNotFoundException {

        Operation operation = operationRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundException("Operation with id:"+ id +" not found")
        );

        operationRepository.delete(operation);

    }


    public String assignOperationToWorkStation(Long id, Long WorkStation_id) throws  RecordNotFoundException {
        Optional<Operation> optionalOperation = operationRepository.findById(id);
        Optional<WorkStation> optionalWorkStations = workStationRepository.findById(WorkStation_id);
        if (optionalOperation.isEmpty() || optionalWorkStations.isEmpty()) {
            throw new RecordNotFoundException("Operation with id:"+ id +" or workstation with id:"+WorkStation_id +" not found");
        }

        Operation operation = optionalOperation.get();
        WorkStation workStation = optionalWorkStations.get();
        operation.setWorkStation(workStation);
        operationRepository.save(operation);

        return  "Operation with id:"+id+" assigned to workstations with id:"+WorkStation_id;
    }


    public OperationDto transferModelToDTO(Operation operation) {
        OperationDto operationDto = new OperationDto();
        operation.setWorkStation(operation.getWorkStation());
        BeanUtils.copyProperties(operation, operationDto);
        return operationDto;
    }

    public Operation transferDtoToModel(OperationDto operationDto) {
        Operation operation = new Operation();
        BeanUtils.copyProperties(operationDto, operation);
        return operation;
    }

}
