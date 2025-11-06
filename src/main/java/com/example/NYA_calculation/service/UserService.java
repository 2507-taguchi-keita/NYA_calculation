package com.example.NYA_calculation.service;

import com.example.NYA_calculation.error.RecordNotFoundException;
import com.example.NYA_calculation.repository.UserRepository;
import com.example.NYA_calculation.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.NYA_calculation.validation.ErrorMessages.E0013;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(E0013));
    }

}
