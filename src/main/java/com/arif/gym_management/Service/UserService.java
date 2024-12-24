package com.arif.gym_management.Service;

import java.util.Optional;

import com.arif.gym_management.Entities.UserEntity;


public interface UserService {

    UserEntity saveUser(UserEntity user);

    Optional<UserEntity> getUserById(int id);

    // add more methods here related user service[logic]

}
