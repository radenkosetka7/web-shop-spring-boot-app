package com.example.webshop.services.impl;

import com.example.webshop.models.entities.LoggerEntity;
import com.example.webshop.repositories.LoggerRepository;
import com.example.webshop.services.LoggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class LoggerServiceImpl implements LoggerService {

    public static final String LEVEL_INFO = "INFO";
    private final LoggerRepository loggerRepository;

    @Override
    public void saveLog(String description,String classLog)
    {
        LoggerEntity loggerEntity= new LoggerEntity();
        loggerEntity.setMessage(description);
        loggerEntity.setLevel(LEVEL_INFO);
        loggerEntity.setDate(new Date());
        loggerEntity.setLog(classLog);
        loggerRepository.saveAndFlush(loggerEntity);
    }
}
