package com.example.logsysteem.services;

import com.example.logsysteem.dtos.ProfilePictureDto;
import com.example.logsysteem.exceptions.RecordNotFoundException;
import com.example.logsysteem.model.ProfilePicture;
import com.example.logsysteem.repository.ProfilePictureRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ProfilePictureService {

    private final ProfilePictureRepository profilePictureRepository;

    public ProfilePictureService(ProfilePictureRepository profilePictureRepository) {
        this.profilePictureRepository = profilePictureRepository;
    }

    public ProfilePictureDto getProfilePicture(Long id) throws RecordNotFoundException {

        Optional<ProfilePicture> optionalProfilePicture = profilePictureRepository.findById(id);

        if (optionalProfilePicture.isEmpty()) {
            throw new RecordNotFoundException("Profile picture not found");
        }

        ProfilePicture profilePicture = optionalProfilePicture.get();

        return transferModelToDTO(profilePicture);
    }

    public ProfilePictureDto uploadProfilePicture(MultipartFile pic) throws IOException {

        if (pic == null) {
            throw new RecordNotFoundException("Picture not found");
        }

        ProfilePictureDto profilePictureDto = new ProfilePictureDto();
        profilePictureDto.setProfilePicture(pic.getBytes());
        profilePictureDto.setFilename(pic.getName());

        ProfilePicture profilePicture = new ProfilePicture();
        profilePicture.setProfilePicture(profilePictureDto.getProfilePicture());
        profilePicture.setFilename(profilePictureDto.getFilename());

        profilePictureRepository.save(profilePicture);

        profilePictureDto.setId(profilePicture.getId());

        return  profilePictureDto;
    }

    public void updateProfilePicture(Long id, MultipartFile pic) throws RecordNotFoundException, IOException {

        Optional<ProfilePicture> optionalProfilePicture = profilePictureRepository.findById(id);

        if (optionalProfilePicture.isEmpty()) {
            throw new RecordNotFoundException("Profile picture not found");
        }

        ProfilePicture profilePicture = optionalProfilePicture.get();

        profilePicture.setProfilePicture(pic.getBytes());
        profilePicture.setFilename(pic.getName());
        profilePicture.setId(id);

        profilePictureRepository.save(profilePicture);
    }

    public void deleteProfilePicture(Long id) throws RecordNotFoundException {

        Optional<ProfilePicture> optionalProfilePicture = profilePictureRepository.findById(id);

        if (optionalProfilePicture.isEmpty()) {
            throw new RecordNotFoundException("Profile picture not found");
        }

        ProfilePicture profilePicture = optionalProfilePicture.get();

        profilePictureRepository.delete(profilePicture);
    }

    public ProfilePictureDto transferModelToDTO(ProfilePicture profilePicture) {
        ProfilePictureDto profilePictureDto = new ProfilePictureDto();
        BeanUtils.copyProperties(profilePicture, profilePictureDto);
        return profilePictureDto;
    }

}
