package com.example.webshop.services.impl;

import com.example.webshop.models.dto.JwtUser;
import com.example.webshop.models.entities.UserEntity;
import com.example.webshop.repositories.UserRepository;
import com.example.webshop.services.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsServiceImpl implements JwtUserDetailsService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return modelMapper.map(userRepository.findByUsernameAndStatus(username, UserEntity.Status.ACTIVE)
                .orElseThrow(()->new UsernameNotFoundException(username)), JwtUser.class);
    }
}
