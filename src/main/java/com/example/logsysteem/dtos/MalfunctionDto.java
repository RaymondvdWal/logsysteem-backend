package com.example.logsysteem.dtos;

import com.example.logsysteem.enums.Status;
import com.example.logsysteem.enums.Urgency;
import com.example.logsysteem.model.WorkStation;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@Setter
public class MalfunctionDto {

    @GeneratedValue
    Long id;
    @Length(max = 250)
    String solution;
    @NotBlank
    @Length(max = 500)
    String description;
    @NotBlank
    @Length(max = 250)
    String action;
    @NotNull
    Status status;
    @NotNull
    Urgency urgency;
    LocalDateTime createMalfunction;
    LocalDateTime updateMalfunction;
    WorkStation workStation;
    String creator;
    String updatedBy;
    @NotBlank @Length(max = 25)
    String title;

}
