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

    @NotBlank
    public String username;
    @NotBlank @Length(min = 10)
    public String password;
    @NotBlank @Length(max = 20)
    public String firstname;
    @NotBlank @Length(max = 25)
    public String lastname;
    public Boolean enabled;
    public String apikey;
    @NotBlank
    public String email;
    public WorkStation workStation;
    public ProfilePicture profilePicture;

    @JsonSerialize
    public Set<Authority> authorities;


    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}