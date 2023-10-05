package com.example.logsysteem.dtos;


import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfilePictureDto {

    private Long id;

    private String filename;
    @Lob
    private byte[] profilePicture;
}
