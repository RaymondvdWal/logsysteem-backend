package com.example.logsysteem.dtos;

import com.example.logsysteem.model.Authority;
import com.example.logsysteem.model.ProfilePicture;
import com.example.logsysteem.model.WorkStation;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
public class UserDto {

    public String username;
    @NotBlank(message =  "voer een wachtwoord in")
    @Length(min = 10, message = "gebruik minimaal 10 karakters")
    public String password;
    @NotBlank(message = "voer een voornaam in")
    @Length(max = 20, message =  "max 20 karakters")
    public String firstname;
    @NotBlank(message = "voer een achternaam in")
    @Length(max = 25, message = "max 25 karakters")
    public String lastname;
    public Boolean enabled;
    public String apikey;
    @NotBlank(message = "voer een email in")
    public String email;
    public WorkStation workStation;
    public ProfilePicture profilePicture;

    @JsonSerialize
    public Set<Authority> authorities;

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}