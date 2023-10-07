package com.example.logsysteem.controllers;


import com.example.logsysteem.dtos.MalfunctionDto;
import com.example.logsysteem.services.MalfunctionService;
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
@RequestMapping("/malfunction")
public class MalfunctionController {

    private final MalfunctionService malfunctionService;
    private final FieldError fieldError = new FieldError();

    @GetMapping
    public ResponseEntity<List<MalfunctionDto>> getAllMalfunctions() {
        List<MalfunctionDto> malfunctions = malfunctionService.getAllMalfunctions();
        return new ResponseEntity<>(malfunctions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MalfunctionDto> getMalfunction(@PathVariable("id") Long id) {
        MalfunctionDto malfunction = malfunctionService.getMalfunction(id);
        return new ResponseEntity<>(malfunction, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<Object> createMalfunction(@Valid @RequestBody MalfunctionDto malfunction, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fieldError.fieldErrorBuilder(bindingResult));
        }
        MalfunctionDto newMalfunction = malfunctionService.createMalfunction(malfunction);
        return new ResponseEntity<>(newMalfunction, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object>  updateMalfunction(@PathVariable("id") Long id, @Valid @RequestBody MalfunctionDto malfunctionDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fieldError.fieldErrorBuilder(bindingResult));
        }
        malfunctionService.updateMalfunction(id, malfunctionDto);
        return ResponseEntity.ok().body(malfunctionDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMalfunction(@PathVariable("id") Long id) {
        malfunctionService.deleteMalfunction(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/workstation/{workstation_id}")
    public ResponseEntity<String> assignMalfunctionToWorkstation(@PathVariable Long id, @PathVariable Long workstation_id) {
        return ResponseEntity.ok().body(malfunctionService.assignMalfunctionToWorkStation(id, workstation_id));
    }
}
