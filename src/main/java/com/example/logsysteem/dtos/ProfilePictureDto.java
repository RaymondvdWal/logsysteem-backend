package com.example.logsysteem.dtos;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfilePictureDto {

    @GeneratedValue
    private Long id;

    private String filename;
    @Lob
    private byte[] profilePicture;
}
