package com.example.logsysteem.model;

import com.example.logsysteem.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name= "operations")
public class Operation {

    @Id
    @GeneratedValue
    public Long id;
    public Date dateIndication;
    public LocalTime timeIndication;
    public String instruction;
    public String comment;
    public Status status;
    public LocalDateTime operationPickedUp;
    public LocalDateTime operationDone;
    public String name;
    public String device;
    public String pickedUpBy;
    public String finishedBy;

    @ManyToOne
    @JoinColumn(name="workstation_id")
    private WorkStation workStation;
}
