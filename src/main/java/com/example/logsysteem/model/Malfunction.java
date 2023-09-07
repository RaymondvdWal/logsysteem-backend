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
   private Long id;
   private String solution;
   private String description;
   private String action;
   private Status status;
   private Urgency urgency;
   private LocalDateTime createMalfunction;
   private LocalDateTime updateMalfunction;
   private String creator;
   private String updatedBy;
   private String title;

    @ManyToOne
    @JoinColumn(name = "workstation_id")
    private WorkStation workStation;

}
