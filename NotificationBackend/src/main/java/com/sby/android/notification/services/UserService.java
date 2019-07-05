package com.sby.android.notification.services;

import com.sby.android.notification.entities.User;
import com.sby.android.notification.exceptions.NotFoundException;
import com.sby.android.notification.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(UUID id) {
        var userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new NotFoundException("Can't find user: " + id.toString());
        }
    }


}
