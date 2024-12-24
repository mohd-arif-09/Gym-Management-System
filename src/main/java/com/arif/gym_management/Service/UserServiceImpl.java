package com.arif.gym_management.Service;

import java.util.List;
import java.util.Optional;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arif.gym_management.Entities.UserEntity;
import com.arif.gym_management.Repository.UserRepository;
import com.arif.gym_management.helper.AppConstants;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //private Logger logger = LoggerFactory.getLogger(this.getClass());


   

    @Override
    public UserEntity saveUser(UserEntity user) {
        // user id : have to generate
        
        // password encode
        // user.setPassword(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // set the user role

        user.setRoleList(List.of(AppConstants.ROLE_ADMIN));
        // logger.info(user.getProvider().toString());
        
              return userRepo.save(user);

    }

    @Transactional
    @Override
    public Optional<UserEntity> getUserById(int id) {
        return userRepo.findById(id);
    }

}