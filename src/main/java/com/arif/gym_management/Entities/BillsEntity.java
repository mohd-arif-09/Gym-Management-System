package com.arif.gym_management.Entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(name="Bills")

public class BillsEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int billid;
    private String name;
    private String email;
    private int amount;
    private String duration;
    private Date billingDate;
    private Date startingDate;
    private Date endDate;
    private Boolean paid;

     @ManyToOne
     private MemberEntity member;
     
     @ManyToOne
     private UserEntity user;
}
