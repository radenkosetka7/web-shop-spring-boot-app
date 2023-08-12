package com.example.webshop.services.impl;

import com.example.webshop.exceptions.NotFoundException;
import com.example.webshop.exceptions.UnauthorizedException;
import com.example.webshop.models.dto.JwtUser;
import com.example.webshop.models.dto.LoginResponse;
import com.example.webshop.models.entities.UserEntity;
import com.example.webshop.models.requests.AccountActivationRequest;
import com.example.webshop.models.requests.LoginRequest;
import com.example.webshop.repositories.UserRepository;
import com.example.webshop.services.AuthService;
import com.example.webshop.services.EmailService;
import com.example.webshop.services.LoggerService;
import com.example.webshop.util.LoggingUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final LoggerService loggerService;

    private Map<String, String> codes = new HashMap<>();

    @Value("${authorization.token.expiration-time}")
    private String tokenExpirationTime;
    @Value("${authorization.token.secret}")
    private String tokenSecret;


    @Override
    public LoginResponse login(LoginRequest request) {
        LoginResponse response = null;
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(), request.getPassword()
                            )
                    );
            JwtUser user = (JwtUser) authenticate.getPrincipal();
            UserEntity userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow(NotFoundException::new);
            response = modelMapper.map(userEntity, LoginResponse.class);
            response.setToken(generateJwt(user));
            loggerService.saveLog("User " + user.getUsername() + " has logged in to the system", this.getClass().getName());
            return response;
        } catch (Exception ex) {
            LoggingUtil.logException(ex, getClass());
            UserEntity userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow(NotFoundException::new);
            if(userEntity.getStatus().equals(UserEntity.Status.REQUESTED))
            {
            loggerService.saveLog("Activation code has sent", this.getClass().getName());
            sendActivationCode(userEntity.getUsername(), userEntity.getMail());
            }
            throw new UnauthorizedException();
        }
    }

    @Override
    public void sendActivationCode(String username, String mail) {
        SecureRandom secureRandom = new SecureRandom();
        String activationCode = String.valueOf(secureRandom.nextInt(9000) + 1000);
        while(codes.containsKey(activationCode))
        {
            activationCode=String.valueOf(secureRandom.nextInt(9000)+1000);
        }
        codes.put(username, activationCode);
        emailService.sendEmail(mail, activationCode);

    }

    @Override
    public boolean activateAccount(AccountActivationRequest request) {
        return codes.containsKey(request.getUsername()) && codes.get(request.getUsername()).equals(request.getCode());
    }


    private String generateJwt(JwtUser user) {
        return Jwts.builder()
                .setId(user.getId().toString())
                .setSubject(user.getUsername())
                .claim("status", user.getUserStatus().name())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(tokenExpirationTime)))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }
}
