package com.krushit.service;

import com.krushit.entity.User;
import com.krushit.entity.UserBuilder;

public interface IUserService {
    boolean registerUser(User user);
}
