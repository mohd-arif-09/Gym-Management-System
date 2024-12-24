package com.arif.gym_management.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import com.arif.gym_management.Entities.BillsEntity;
import com.arif.gym_management.Entities.MemberEntity;
import com.arif.gym_management.Repository.BillsRepository;
import com.arif.gym_management.Repository.MemberRepository;

@Controller
@RequestMapping("/member")
public class MemberController {
    
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BillsRepository billsRepository;

    @GetMapping("/index")
    public String dashboard(Model model,Principal principal)
    {
        model.addAttribute("title","Member Dashboard ");
       return "Member/member_dashboard";
    }

    @GetMapping("/details")
    public String getMemberDetails(Principal principal,Model model) {
        String email = principal.getName();
        MemberEntity member = this.memberRepository.findByEmail(email)

                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
      
                model.addAttribute("member",member);
                model.addAttribute("title","Member Details ");
                return "Member/member_details"; 
    }

    @GetMapping("/bills")
    public String getMemberBills(Principal principal,Model model) {
        String email = principal.getName();
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        List<BillsEntity> bills = billsRepository.findByMember(member);
         model.addAttribute("bills",bills);
          model.addAttribute("title","Member Bills ");

         return "Member/member_bills";
    }
}
