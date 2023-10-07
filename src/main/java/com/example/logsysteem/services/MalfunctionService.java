package com.example.logsysteem.services;

import com.example.logsysteem.dtos.MalfunctionDto;
import com.example.logsysteem.exceptions.RecordNotFoundException;
import com.example.logsysteem.model.Malfunction;
import com.example.logsysteem.model.WorkStation;
import com.example.logsysteem.repository.MalfunctionRepository;
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
public class MalfunctionService {

    private final MalfunctionRepository malfunctionRepository;
    private final WorkstationRepository workStationRepository;

    public MalfunctionService(MalfunctionRepository malfunctionRepository, WorkstationRepository workStationRepository) {
        this.malfunctionRepository = malfunctionRepository;
        this.workStationRepository = workStationRepository;
    }

    public List<MalfunctionDto> getAllMalfunctions() throws RecordNotFoundException {

        List<MalfunctionDto> malfunctionDtoList = new ArrayList<>();
        List<Malfunction> malfunctions = malfunctionRepository.findAll();

        if (malfunctions.isEmpty()) {
            throw new RecordNotFoundException("No malfunctions found");
        }

        for (Malfunction malfunction : malfunctions) {
            malfunctionDtoList.add(transferModelToDTO(malfunction));
        }

        return malfunctionDtoList;
    }

    public MalfunctionDto getMalfunction(Long id) throws RecordNotFoundException{

        Optional<Malfunction> optionalMalfunction = malfunctionRepository.findById(id);

        if (optionalMalfunction.isEmpty()) {
            throw new RecordNotFoundException("Malfunction with id:"+ id +" not found");
        }

        Malfunction malfunction = optionalMalfunction.get();

        return transferModelToDTO(malfunction);
    }

    public MalfunctionDto createMalfunction(MalfunctionDto malfunctionDto) {
        Malfunction malfunction = transferDtoToModel(malfunctionDto);

        malfunction.setCreateMalfunction(LocalDateTime.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            malfunction.setCreator(authentication.getName());
        }

        malfunction = malfunctionRepository.save(malfunction);

        return transferModelToDTO(malfunction);
    }

    public void updateMalfunction(Long id, @RequestBody MalfunctionDto malfunctionDto ) throws RecordNotFoundException {

        Malfunction malfunction = malfunctionRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundException("Malfunction with id:"+ id +" not found")
        );

        malfunction.setId(id);
        malfunction.setSolution(malfunctionDto.getSolution());
        malfunction.setDescription(malfunctionDto.getDescription());
        malfunction.setAction(malfunctionDto.getAction());
        malfunction.setStatus(malfunctionDto.getStatus());
        malfunction.setUrgency(malfunctionDto.getUrgency());
        malfunction.setUpdateMalfunction(LocalDateTime.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            malfunction.setUpdatedBy(authentication.getName());
        }

        malfunctionRepository.save(malfunction);

    }

    public void deleteMalfunction(Long id) throws RecordNotFoundException {

        Malfunction malfunction = malfunctionRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundException("Malfunction with id:"+ id +" not found")
        );

        malfunctionRepository.delete(malfunction);

    }

    public String assignMalfunctionToWorkStation(Long id, Long workStation_id) throws  RecordNotFoundException {
        Optional<Malfunction> optionalMalfunction = malfunctionRepository.findById(id);
        Optional<WorkStation> optionalWorkStations = workStationRepository.findById(workStation_id);
        if (optionalMalfunction.isEmpty() || optionalWorkStations.isEmpty()) {
            throw new RecordNotFoundException("Malfunction with id:"+ id +" or workstation with id:"+workStation_id +" not found");
        }

        Malfunction malfunction = optionalMalfunction.get();
        WorkStation workStation = optionalWorkStations.get();
        malfunction.setWorkStation(workStation);
        malfunctionRepository.save(malfunction);

        return  "Malfunction with id:"+id+" assigned to workstations with id:"+workStation_id;
    }

    public MalfunctionDto transferModelToDTO(Malfunction malfunction) {
        MalfunctionDto malfunctionDto = new MalfunctionDto();
        malfunction.setWorkStation(malfunction.getWorkStation());
        BeanUtils.copyProperties(malfunction, malfunctionDto);
        return malfunctionDto;
    }

    public Malfunction transferDtoToModel(MalfunctionDto malfunctionDto) {
        Malfunction malfunction = new Malfunction();
        BeanUtils.copyProperties(malfunctionDto, malfunction);
        return malfunction;
    }
}
