package com.arif.gym_management.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arif.gym_management.Entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndPassword(String email, String password);

    @Query("select u from UserEntity u where u.email =:email")
    
    public UserEntity getUserEntityByUserName(@Param("email") String email);
    
}
