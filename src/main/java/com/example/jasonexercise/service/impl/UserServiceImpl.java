package com.example.jasonexercise.service.impl;

import com.example.jasonexercise.constants.GlobalConstant;
import com.example.jasonexercise.model.dto.UserSeedDto;
import com.example.jasonexercise.model.dto.UserSoldDTO;
import com.example.jasonexercise.model.entity.User;
import com.example.jasonexercise.repository.UserRepository;
import com.example.jasonexercise.service.UserService;
import com.example.jasonexercise.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    public static final String USERS_FILE_NAME = "users.json";

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public void seedUsers() throws IOException {

        if (userRepository.count() == 0) {
            Arrays.stream(gson.fromJson(Files.readString(Path.of(GlobalConstant.RESOURCE_FILE_PATH + USERS_FILE_NAME)),
                            UserSeedDto[].class))
                    .filter(validationUtil::isValid)
                    .map(userSeedDto -> modelMapper.map(userSeedDto, User.class))
                    .forEach(userRepository::save);
        }

    }

    @Override
    public User findByRandomUser() {
        long randomId = ThreadLocalRandom
                .current()
                .nextLong(1, userRepository.count() + 1);

        return userRepository
                .findById(randomId)
                .orElse(null);
    }

    @Override
    public List<UserSoldDTO> findAllUsersWithMoreThanOneSoldProducts() {

        return userRepository
                .findAllUsersWithMoreThanOneMoreProductOrderByLastNameThanFirstName()
                .stream()
                .map(user -> modelMapper.map(user,UserSoldDTO.class))
                .collect(Collectors.toList());
    }
}
