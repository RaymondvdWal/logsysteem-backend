package com.example.logsysteem.dtos;

import com.example.logsysteem.enums.Status;
import com.example.logsysteem.model.WorkStation;
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

    public Long id;
    public Date dateIndication;
    public LocalTime timeIndication;
    @NotBlank(message = "Voer een instructie in")
    public String instruction;
    @Length(max=250, message = "max 250 karakters")
    public String comment;
    @NotNull(message =  "Selecteer een status")
    public Status status;
    public LocalDateTime operationPickedUp;
    public LocalDateTime operationDone;
    @NotBlank(message = "Voer een naam in")
    @Length(max = 25, message = "max 25 karakters")
    public String name;
    @NotBlank(message = "Voer een apparaat in")
    @Length(max = 25, message = "max 25 karakters")
    public String device;
    public WorkStation workStation;
    public String pickedUpBy;
    public String finishedBy;
}
