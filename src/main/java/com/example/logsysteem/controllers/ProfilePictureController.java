package com.example.logsysteem.controllers;

import com.example.logsysteem.dtos.ProfilePictureDto;
import com.example.logsysteem.exceptions.RecordNotFoundException;
import com.example.logsysteem.services.ProfilePictureService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/pic")
public class ProfilePictureController {

   private final ProfilePictureService profilePictureService;

    public ProfilePictureController(ProfilePictureService profilePictureService) {
        this.profilePictureService = profilePictureService;
    }

    @PostMapping("/new")
    public ResponseEntity<ProfilePictureDto> uploadProfilePicture(@RequestParam MultipartFile pic) throws IOException {
        ProfilePictureDto newPicture = profilePictureService.uploadProfilePicture(pic);
        return new ResponseEntity<>(newPicture,HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable("id") Long id) {

        ProfilePictureDto profilePictureDto = profilePictureService.getProfilePicture(id);

        byte[] profilePictureBytes = profilePictureDto.getProfilePicture();

        if (profilePictureBytes == null) {
            throw new RecordNotFoundException("File not found");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("attachment", "file" + profilePictureDto.getFilename() + ".png");
        headers.setContentLength(profilePictureBytes.length);

        return new ResponseEntity<>(profilePictureBytes,headers, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> changeProfilePicture(@PathVariable("id") Long id, @RequestParam MultipartFile newPic) throws IOException {
        profilePictureService.updateProfilePicture(id, newPic);
        return ResponseEntity.ok().body("Successfully changed your profile picture");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProfilePicture(@PathVariable("id") Long id) {
        profilePictureService.deleteProfilePicture(id);
        return ResponseEntity.noContent().build();
    }
}
