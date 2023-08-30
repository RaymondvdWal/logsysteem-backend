package com.example.logsysteem.dtos;


import com.example.logsysteem.enums.Location;
import com.example.logsysteem.model.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class WorkStationDto {

    @GeneratedValue
    public Long id;
    @NotBlank
    public String name;
    @Length(max=200)
    public String pushMessage;
    @Length(max=400)
    public String generalMessage;
    @NotNull
    @Enumerated(EnumType.STRING)
    public Location location;
    public User user;
}
