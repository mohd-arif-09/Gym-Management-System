package com.arif.gym_management.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arif.gym_management.Entities.MemberEntity;
import com.arif.gym_management.Entities.UserEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity , Integer>{
    
    @Query("from MemberEntity as c where c.user.id=:userId")

    //current page
    //current per page
    public Page<MemberEntity> findMemberEntitiesByUserEntity(@Param("userId")int userId, Pageable pageable);

    // search 
    public List<MemberEntity> findByNameContainingAndUser(String name,UserEntity user);

    @Query("select m from MemberEntity m where m.email =:email")

    public MemberEntity getMemberEntityByUserName(@Param("email") String email);
    
    Optional<MemberEntity> findByEmail(@Param("email") String email);
    
    Optional<MemberEntity> findById(Integer id);

}
