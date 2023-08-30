package com.example.logsysteem.config;

import com.example.logsysteem.filter.JwtRequestFilter;
import com.example.logsysteem.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }


    // Authenticatie met customUserDetailsService en passwordEncoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }


    // PasswordEncoderBean. Deze kun je overal in je applicatie injecteren waar nodig.
    // Je kunt dit ook in een aparte configuratie klasse zetten.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authorizatie met jwt
    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {

        //JWT token authentication
        http
                .csrf().disable()
                .httpBasic().disable()
                .cors().and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/users").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"/users").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/users/**").authenticated()
                .requestMatchers(HttpMethod.POST,"/users/user").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST,"/users/moderator").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST,"/users/admin").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/users/{username}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/users/{username}/profilePicture/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/malfunction/**").hasAuthority("MODERATOR")
                .requestMatchers(HttpMethod.GET, "/malfunction").hasAnyAuthority("USER", "MODERATOR")
                .requestMatchers(HttpMethod.GET, "/malfunction/**").hasAnyAuthority("USER", "MODERATOR")
                .requestMatchers(HttpMethod.POST, "/malfunction/**").hasAnyAuthority("USER", "MODERATOR")
                .requestMatchers(HttpMethod.PUT, "/malfunction/**").hasAnyAuthority("USER", "MODERATOR")
                .requestMatchers(HttpMethod.PUT, "malfunction/{id}/workstation/{workstation_id}").hasAnyAuthority("USER", "MODERATOR")
                .requestMatchers(HttpMethod.POST, "/operation/**").hasAuthority( "MODERATOR")
                .requestMatchers(HttpMethod.PUT, "/operation/**").hasAnyAuthority("USER", "MODERATOR")
                .requestMatchers(HttpMethod.DELETE, "/operation/**").hasAuthority("MODERATOR")
                .requestMatchers(HttpMethod.GET, "/operation").hasAnyAuthority("USER", "MODERATOR")
                .requestMatchers(HttpMethod.GET, "/operation/**").hasAnyAuthority("USER", "MODERATOR")
                .requestMatchers(HttpMethod.GET, "/workstation").hasAnyAuthority("USER", "MODERATOR")
                .requestMatchers(HttpMethod.GET, "/workstation/**").hasAnyAuthority("USER", "MODERATOR")
                .requestMatchers(HttpMethod.POST, "/workstation/new").hasAuthority("MODERATOR")
                .requestMatchers(HttpMethod.PUT, "/workstation/**").hasAnyAuthority("MODERATOR", "USER")
                .requestMatchers(HttpMethod.DELETE, "/workstation/**").hasAuthority("MODERATOR")
                .requestMatchers(HttpMethod.GET, "/pic/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/pic/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/pic/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/pic/**").authenticated()

                .requestMatchers("/authenticated").authenticated()
                .requestMatchers("/authenticate").permitAll()
                .anyRequest().denyAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}