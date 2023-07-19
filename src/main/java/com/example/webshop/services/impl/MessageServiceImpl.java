package com.example.webshop.services.impl;

import com.example.webshop.exceptions.NotFoundException;
import com.example.webshop.models.dto.JwtUser;
import com.example.webshop.models.dto.Message;
import com.example.webshop.models.entities.MessageEntity;
import com.example.webshop.models.entities.UserEntity;
import com.example.webshop.models.requests.MessageRequest;
import com.example.webshop.repositories.MessageRepository;
import com.example.webshop.repositories.UserRepository;
import com.example.webshop.services.LoggerService;
import com.example.webshop.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final LoggerService loggerService;


    @Override
    public Message insert(MessageRequest messageRequest,Authentication authentication) {

        JwtUser user =(JwtUser)authentication.getPrincipal();
        UserEntity userEntity=userRepository.findById(user.getId()).orElseThrow(NotFoundException::new);
        MessageEntity messageEntity = modelMapper.map(messageRequest, MessageEntity.class);
        messageEntity.setId(null);
        messageEntity.setUser(userEntity);
        messageEntity.setStatus(false);
        loggerService.saveLog("User: " + user.getUsername() + " has sent message to customer support.",this.getClass().getName());
        //izvuci user principal
        return modelMapper.map(messageRepository.saveAndFlush(messageEntity), com.example.webshop.models.dto.Message.class);
    }
}
