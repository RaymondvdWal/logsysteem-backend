package com.example.logsysteem.dtos;


import com.example.logsysteem.enums.Location;
import com.example.logsysteem.model.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class WorkStationDto {

    public Long id;
    @NotBlank (message = "voer een naam in")
    public String name;
    @Length(max=200, message = "max 200 karakters")
    public String pushMessage;
    @Length(max=400, message = "max 400 karakters")
    public String generalMessage;
    @NotNull (message = "voer een locatie in")
    @Enumerated(EnumType.STRING)
    public Location location;
    public User user;
}
