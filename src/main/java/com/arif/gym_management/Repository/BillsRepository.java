package com.arif.gym_management.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arif.gym_management.Entities.BillsEntity;
import com.arif.gym_management.Entities.MemberEntity;

@Repository

public interface BillsRepository extends JpaRepository<BillsEntity,Integer > {

   @Query("SELECT b FROM BillsEntity b WHERE b.member.user.id = :userId")
   Page<BillsEntity> findAllBillsByUserId(@Param("userId") Integer userId, Pageable pageable);

   

   @Query("SELECT b FROM BillsEntity b WHERE b.member.memid = :memid")
   BillsEntity findByMemberId(@Param("memid") int memid);


     public List<BillsEntity> findByNameContainingAndUser(String name,MemberEntity member);
   
    // this to protect duplicate bill creation
    List<BillsEntity> findByMember(MemberEntity member);
    Optional<BillsEntity> findByBillid(int billid);

}
