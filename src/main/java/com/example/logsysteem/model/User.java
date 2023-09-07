package com.example.logsysteem.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Getter
    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Getter
    @Column(nullable = false, length = 255)
    private String password;

    private String firstname;

    private String lastname;

    @Getter
    @Column(nullable = false)
    private boolean enabled = true;

    @Getter
    @Column
    private String apikey;

    @Getter
    @Column
    private String email;

    @Getter
    @OneToMany(
            targetEntity = Authority.class,
            mappedBy = "username",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.EAGER
    )

    @JsonIgnore
    @JoinTable(
            name = "workstation_users",
            joinColumns = { @JoinColumn(name = "username") },
            inverseJoinColumns = { @JoinColumn(name = "workstation_id") }
    )
    private List<WorkStation> workStations;

    @JsonIgnore
    @OneToOne
    private ProfilePicture profilePicture;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public void setApikey(String apikey) { this.apikey = apikey; }

    public void setEmail(String email) { this.email = email;}

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }
    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
    }

}