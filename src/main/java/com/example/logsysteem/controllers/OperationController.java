package com.example.logsysteem.controllers;

import com.example.logsysteem.dtos.OperationDto;
import com.example.logsysteem.services.OperationService;
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
@RequestMapping("/operation")
public class OperationController {

    private final OperationService operationService;
    private final FieldError fieldError = new FieldError();

    @GetMapping
    public ResponseEntity<List<OperationDto>> getAllOperations() {
        List<OperationDto> operations = operationService.getAllOperations();
        return new ResponseEntity<>(operations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationDto> getOperation(@PathVariable("id") Long id) {
        OperationDto operation = operationService.getOperation(id);
        return new ResponseEntity<>(operation, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<Object> createOperation(@Valid @RequestBody OperationDto operation, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fieldError.fieldErrorBuilder(bindingResult));
        }
        OperationDto newOperation = operationService.createOperation(operation);
        return new ResponseEntity<>(newOperation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object>  updateOperation(@PathVariable("id") Long id, @Valid @RequestBody OperationDto operationDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fieldError.fieldErrorBuilder(bindingResult));
        }
        operationService.updateOperation(id, operationDto);
        return ResponseEntity.ok().body(operationDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOperation(@PathVariable("id") Long id) {
        operationService.deleteOperation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/workstation/{workstation_id}")
    public ResponseEntity<String> assignOperationToWorkstation(@PathVariable Long id, @PathVariable Long workstation_id) {
        return ResponseEntity.ok(operationService.assignOperationToWorkStation(id, workstation_id));
    }
}
