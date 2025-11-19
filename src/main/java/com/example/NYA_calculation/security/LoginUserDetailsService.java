package com.example.NYA_calculation.security;

import com.example.NYA_calculation.repository.UserRepository;
import com.example.NYA_calculation.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByAccount(account);
        return user
                .map(LoginUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("not found account : " + account));
    }

}
