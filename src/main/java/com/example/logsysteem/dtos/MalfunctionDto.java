package com.example.logsysteem.dtos;

import com.example.logsysteem.enums.Status;
import com.example.logsysteem.enums.Urgency;
import com.example.logsysteem.model.WorkStation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@Setter
public class MalfunctionDto {

    Long id;
    @Length(max = 250, message = "max 250 karakters")
    String solution;
    @NotBlank(message = "voer een beschrijving in")
    @Length(max = 500, message = "max 500 karakters")
    String description;
    @NotBlank(message = "voer een actie in")
    @Length(max = 250,  message = "max 250 karakters")
    String action;
    @NotNull(message = "selecteer een status")
    Status status;
    @NotNull(message = "selecteer een prioriteit")
    Urgency urgency;
    LocalDateTime createMalfunction;
    LocalDateTime updateMalfunction;
    WorkStation workStation;
    String creator;
    String updatedBy;
    @NotBlank @Length(max = 25, message =  "max 25 karakters")
    String title;

}
