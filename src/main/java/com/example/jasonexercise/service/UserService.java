package com.example.jasonexercise.service;

import com.example.jasonexercise.model.dto.UserSoldDTO;
import com.example.jasonexercise.model.entity.User;

import java.io.IOException;
import java.util.List;

public interface UserService {
    void seedUsers() throws IOException;

    User findByRandomUser();

    List<UserSoldDTO> findAllUsersWithMoreThanOneSoldProducts();
}
