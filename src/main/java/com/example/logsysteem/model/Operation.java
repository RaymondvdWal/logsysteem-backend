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
    private Long id;
    private Date dateIndication;
    private LocalTime timeIndication;
    private String instruction;
    private String comment;
    private Status status;
    private LocalDateTime operationPickedUp;
    private LocalDateTime operationDone;
    private String name;
    private String device;
    private String pickedUpBy;
    private String finishedBy;

    @ManyToOne
    @JoinColumn(name="workstation_id")
    private WorkStation workStation;
}
