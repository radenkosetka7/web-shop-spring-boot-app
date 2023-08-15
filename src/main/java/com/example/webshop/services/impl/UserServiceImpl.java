package com.example.webshop.services.impl;

import com.example.webshop.exceptions.BadRequestException;
import com.example.webshop.exceptions.ConflictException;
import com.example.webshop.exceptions.NotFoundException;
import com.example.webshop.models.dto.JwtUser;
import com.example.webshop.models.dto.LoginResponse;
import com.example.webshop.models.dto.Product;
import com.example.webshop.models.dto.User;
import com.example.webshop.models.entities.EmpoyeeEntity;
import com.example.webshop.models.entities.UserEntity;
import com.example.webshop.models.enums.Role;
import com.example.webshop.models.requests.ChangePasswordRequest;
import com.example.webshop.models.requests.SignUpRequest;
import com.example.webshop.models.requests.UserRequest;
import com.example.webshop.repositories.EmployeeRepository;
import com.example.webshop.repositories.UserRepository;
import com.example.webshop.services.AuthService;
import com.example.webshop.services.LoggerService;
import com.example.webshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final LoggerService loggerService;
    private final EmployeeRepository employeeRepository;


    @Value("${authorization.default.username-admin:}")
    private String defaultUsernameAdmin;
    @Value("${authorization.default.first-name-admin:}")
    private String defaultFirstNameAdmin;
    @Value("${authorization.default.last-name-admin:}")
    private String defaultLastNameAdmin;
    @Value("${authorization.default.password-admin:}")
    private String defaultPasswordAdmin;


    @Value("${authorization.default.username-support:}")
    private String defaultUsernameSupport;
    @Value("${authorization.default.first-name-support:}")
    private String defaultFirstNameSupport;
    @Value("${authorization.default.last-name-support:}")
    private String defaultLastNameSupport;
    @Value("${authorization.default.password-support:}")
    private String defaultPasswordSupport;

    @Value("${avatarDir:}")
    private String dir;

    @PostConstruct
    public void postConstruct() {

        if (employeeRepository.count() == 0) {
            EmpoyeeEntity admin = new EmpoyeeEntity();
            EmpoyeeEntity support = new EmpoyeeEntity();
            admin.setUsername(defaultUsernameAdmin);
            admin.setPassword(passwordEncoder.encode(defaultPasswordAdmin));
            admin.setFirstname(defaultFirstNameAdmin);
            admin.setSurname(defaultLastNameAdmin);
            admin.setRole(Role.ADMIN);
            support.setUsername(defaultUsernameSupport);
            support.setPassword(passwordEncoder.encode(defaultPasswordSupport));
            support.setFirstname(defaultFirstNameSupport);
            support.setSurname(defaultLastNameSupport);
            support.setRole(Role.SUPPORT);
            employeeRepository.saveAndFlush(admin);
            employeeRepository.saveAndFlush(support);
        }
    }

    @Override
    public Page<Product> getAllProductsForBuyer(Pageable page, Authentication authentication, String title) {
        JwtUser user = (JwtUser) authentication.getPrincipal();
        loggerService.saveLog("User: " + user.getUsername() + " has searched his purchased products.", this.getClass().getName());
        if (title == null || title.isEmpty()) {
            return userRepository.getAllProductsForBuyer(page, user.getId()).map(p -> modelMapper.map(p, Product.class));
        } else {
            return userRepository.getAllProductsForBuyerAndSearch(page, user.getId(), title).map(p -> modelMapper.map(p, Product.class));
        }
    }

    @Override
    public Page<Product> getAllProductsForSeller(Pageable page, Integer finished, Authentication authentication, String title) {
        JwtUser user = (JwtUser) authentication.getPrincipal();
        loggerService.saveLog("User: " + user.getUsername() + " has searched his sold products.", this.getClass().getName());
        if (title == null || title.isEmpty()) {
            return userRepository.getAllProductsForSeller(page, user.getId(), finished).map(p -> modelMapper.map(p, Product.class));
        } else {
            return userRepository.getAllProductsForSellerSearch(page, user.getId(), finished, title).map(p -> modelMapper.map(p, Product.class));

        }
    }

    @Override
    public User findById(Integer id) {
        return modelMapper.map(userRepository.findById(id).orElseThrow(NotFoundException::new), User.class);
    }

    @Override
    public User update(Integer id, UserRequest userRequest) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(NotFoundException::new);
        userEntity.setAvatar(userRequest.getAvatar());
        userEntity.setName(userRequest.getName());
        userEntity.setSurname(userRequest.getSurname());
        userEntity.setCity(userRequest.getCity());
        userEntity.setMail(userRequest.getMail());
        loggerService.saveLog("User: " + userEntity.getUsername() + " has updated profile.", this.getClass().getName());
        return modelMapper.map(userRepository.saveAndFlush(userEntity), User.class);
    }


    @Override
    public String uploadImage(MultipartFile file) {
        try {
            String imageName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path imagePath = Paths.get(dir + imageName);
            Files.copy(file.getInputStream(), imagePath);
            return imageName;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public User changePassword(Integer id, ChangePasswordRequest changePasswordRequest) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(NotFoundException::new);
        if (changePasswordRequest.getPassword().equals(changePasswordRequest.getNewPassword())) {
            userEntity.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
            loggerService.saveLog("User: " + userEntity.getUsername() + " has changed password.", this.getClass().getName());
            return modelMapper.map(userRepository.saveAndFlush(userEntity), User.class);
        } else {
            throw new BadRequestException();
        }
    }


    @Override
    public LoginResponse findById(Integer id, Class<LoginResponse> response) throws NotFoundException {
        return modelMapper.map(userRepository.findById(id).orElseThrow(NotFoundException::new), LoginResponse.class);
    }

    @Override
    public void signUp(SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername()) && userRepository.existsByMail(request.getMail()))
            throw new ConflictException();
        UserEntity entity = modelMapper.map(request, UserEntity.class);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setStatus(UserEntity.Status.REQUESTED);
        entity = userRepository.saveAndFlush(entity);
        loggerService.saveLog("New user: " + entity.getUsername() + " has registered.", this.getClass().getName());
        authService.sendActivationCode(entity.getUsername(), entity.getMail());
    }

    @Override
    public User activateAccount(String username) {
        UserEntity userEntity = userRepository.findByUsernameAndStatus(username, UserEntity.Status.REQUESTED).orElseThrow(NotFoundException::new);
        userEntity.setStatus(UserEntity.Status.ACTIVE);
        loggerService.saveLog("User: " + userEntity.getUsername() + " has activated profile.", this.getClass().getName());
        return modelMapper.map(userRepository.saveAndFlush(userEntity), User.class);
    }
}
