package com.example.logsysteem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProfilePicture {

    @Id
    @GeneratedValue
    private Long id;

    private String filename;

    @Lob
    private byte[] profilePicture;

    @OneToOne (mappedBy = "profilePicture")
    private User user;
}
