package com.example.logsysteem.model;

import com.example.logsysteem.enums.Status;
import com.example.logsysteem.enums.Urgency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name= "malfunctions")
public class Malfunction {

    @Id
    @GeneratedValue
    Long id;
    String solution;
    String description;
    String action;
    Status status;
    Urgency urgency;
    LocalDateTime createMalfunction;
    LocalDateTime updateMalfunction;
    String creator;
    String updatedBy;
    String title;

    @ManyToOne
    @JoinColumn(name = "workstation_id")
    private WorkStation workStation;

}
