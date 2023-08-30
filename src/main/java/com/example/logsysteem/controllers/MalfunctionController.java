package com.example.logsysteem.controllers;


import com.example.logsysteem.dtos.MalfunctionDto;
import com.example.logsysteem.services.MalfunctionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/malfunction")
public class MalfunctionController {

    private MalfunctionService malfunctionService;

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
    public ResponseEntity<MalfunctionDto> createMalfunction(@RequestBody MalfunctionDto malfunction) {
        MalfunctionDto newMalfunction = malfunctionService.createMalfunction(malfunction);
        return new ResponseEntity<>(newMalfunction, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MalfunctionDto>  updateMalfunction(@PathVariable("id") Long id, @Valid @RequestBody MalfunctionDto malfunctionDto) {
        malfunctionService.updateMalfunction(id, malfunctionDto);
        return ResponseEntity.ok().body(malfunctionDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMalfunction(@PathVariable("id") Long id) {
        malfunctionService.deleteMalfunction(id);
        return ResponseEntity.ok().body("Malfunction successfully deleted");
    }

    @PutMapping("/{id}/workstation/{workstation_id}")
    public ResponseEntity<String> assignMalfunctionToWorkstation(@PathVariable Long id, @PathVariable Long workstation_id) {
        return ResponseEntity.ok().body(malfunctionService.assignMalfunctionToWorkStation(id, workstation_id));
    }
}
