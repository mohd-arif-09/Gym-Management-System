package com.arif.gym_management.Service;


import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.arif.gym_management.Entities.MemberEntity;
import com.arif.gym_management.Repository.MemberRepository;

@Service
public class MemberDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // apne user ko load karana hai
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));

    return new org.springframework.security.core.userdetails.User(member.getEmail(), member.getPassword(), getAuthorities(member)); 
} 

private Collection<? extends GrantedAuthority> getAuthorities(MemberEntity member) {
 return List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole()));

} 

}