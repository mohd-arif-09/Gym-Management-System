package com.arif.gym_management.Entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
//import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@ToString

@Table(name="Member")
public class MemberEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int memid;
    private String name;
    
    @Size(min = 10,max = 10, message = "only 10 digit number are allowed !!")
    private String phone;

    @Column(unique = true)
    @Email (regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "Invalid Email !!")  
    private String email;

    private String password;

    private String duration;
    private String role;
    @Column(length = 100)
    private String address; 


    @ManyToOne
    @JsonIgnore
    private UserEntity user;
    
     @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER ,mappedBy = "member" )
    @JsonIgnore
    private List<BillsEntity> bills= new ArrayList<>();


    @Override
    public boolean equals(Object obj){
        return this.memid==((MemberEntity)obj).getMemid();
    }

    public List<BillsEntity> getBills() {
        return bills;
    }

    public void setBills(List<BillsEntity> bills) {
        this.bills = bills;
    }

   @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // list of roles[USER,ADMIN]
        // Collection of SimpGrantedAuthority[roles{ADMIN,USER}]
        Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
        return roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
