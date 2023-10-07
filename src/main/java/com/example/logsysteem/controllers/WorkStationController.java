package com.example.logsysteem.controllers;


import com.example.logsysteem.dtos.WorkStationDto;
import com.example.logsysteem.services.WorkStationService;
import com.example.logsysteem.utils.FieldError;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/workstation")
public class WorkStationController {

    private final WorkStationService workStationService;
    private final FieldError fieldError = new FieldError();

    @GetMapping
    public ResponseEntity<List<WorkStationDto>> getAllWorkStations() {
        List<WorkStationDto> workStations = workStationService.getAllWorkStations();
        return new ResponseEntity<>(workStations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkStationDto> getWorkStations(@PathVariable("id") Long id) {
        WorkStationDto workStations = workStationService.getWorkStation(id);
        return new ResponseEntity<>(workStations, HttpStatus.OK);
    }


    @PostMapping("/new")
    public ResponseEntity<Object> createWorkStations(@Valid @RequestBody WorkStationDto workStation, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(fieldError.fieldErrorBuilder(bindingResult));
        }
        WorkStationDto newWorkStation = workStationService.createWorkStation(workStation);
        return new ResponseEntity<>(newWorkStation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object>  updateWorkStations(@PathVariable("id") Long id, @Valid @RequestBody WorkStationDto workStationDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(fieldError.fieldErrorBuilder(bindingResult));
        }
        workStationService.updateWorkstation(id, workStationDto);
        return ResponseEntity.ok().body(workStationDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkStations(@PathVariable("id") Long id) {
        workStationService.deleteWorkStation(id);
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/user/{username}")
    public ResponseEntity<String> assignUserToWorkStation(@PathVariable("id") Long id, @PathVariable("username") String username) {
        return ResponseEntity.ok(workStationService.assignUserToWorkStation(id, username));
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<String>> getUsersAssignedToWorkstation(@PathVariable("id") Long id) {
        List<String> usersAssignedToWorkstation = workStationService.usersAssignedToWorkstation(id);
        return ResponseEntity.ok().body(usersAssignedToWorkstation);
    }

}
