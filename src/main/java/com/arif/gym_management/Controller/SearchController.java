package com.arif.gym_management.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.arif.gym_management.Entities.BillsEntity;
import com.arif.gym_management.Entities.MemberEntity;
import com.arif.gym_management.Entities.UserEntity;
import com.arif.gym_management.Repository.BillsRepository;
import com.arif.gym_management.Repository.MemberRepository;
import com.arif.gym_management.Repository.UserRepository;

@RestController
public class SearchController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BillsRepository billsRepository;


    // search handler member
    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal)
    {
      UserEntity user=this.userRepository.getUserEntityByUserName(principal.getName());
      List<MemberEntity> member = this.memberRepository.findByNameContainingAndUser(query, user);
        
      return ResponseEntity.ok(member);
    }

    // search handler for bills 

    @GetMapping("/search-bill/{query}")
    public ResponseEntity<?> searchbills(@PathVariable("query") String query,Principal principal)
    {
      MemberEntity user=this.memberRepository.getMemberEntityByUserName(principal.getName());

      List<BillsEntity> bills = this.billsRepository.findByNameContainingAndUser(query, user);
        
      return ResponseEntity.ok(bills);
    }

}
