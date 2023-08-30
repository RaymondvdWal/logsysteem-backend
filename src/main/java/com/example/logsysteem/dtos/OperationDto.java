package com.example.logsysteem.dtos;

import com.example.logsysteem.enums.Status;
import com.example.logsysteem.model.WorkStation;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
public class OperationDto {

    @GeneratedValue
    public Long id;

    public Date dateIndication;
    public LocalTime timeIndication;
    @NotBlank
    public String instruction;
    @Length(max=250)
    public String comment;
    @NotNull
    public Status status;
    public LocalDateTime operationPickedUp;
    public LocalDateTime operationDone;
    @NotBlank @Length(max = 25)
    public String name;
    @NotBlank @Length(max = 25)
    public String device;
    public WorkStation workStation;
    public String pickedUpBy;
    public String finishedBy;
}
