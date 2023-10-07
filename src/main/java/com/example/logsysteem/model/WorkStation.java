package com.example.logsysteem.model;

import com.example.logsysteem.enums.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name= "workstations")
public class WorkStation {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String pushMessage;
    private String generalMessage;
    private Location location;

    @ManyToMany(
            mappedBy = "workStations",
            fetch = FetchType.EAGER
    )
    @JsonIgnore
    private List<User> users;

    @OneToMany(mappedBy = "workStation")
    @JsonIgnore
    private List<Operation> operations;

    @OneToMany(mappedBy = "workStation")
    @JsonIgnore
    private List<Malfunction> malfunctions;
}
