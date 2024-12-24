package com.arif.gym_management.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.arif.gym_management.Entities.MemberEntity;
import com.arif.gym_management.Entities.UserEntity;
import com.arif.gym_management.Repository.MemberRepository;
import com.arif.gym_management.Repository.UserRepository;

@Service
public class CombinedUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Pehle UserEntity me email ko search karein
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    user.getAuthorities()
            );
        }

        // Agar UserEntity me nahi mila, to MemberEntity me search karein
        MemberEntity member = memberRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(
                member.getEmail(),
                member.getPassword(),
                member.getAuthorities()
        );
    }
}
