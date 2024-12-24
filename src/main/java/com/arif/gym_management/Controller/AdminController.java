package com.arif.gym_management.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.arif.gym_management.Entities.BillsEntity;
import com.arif.gym_management.Entities.MemberEntity;
import com.arif.gym_management.Entities.UserEntity;
import com.arif.gym_management.Repository.BillsRepository;
import com.arif.gym_management.Repository.MemberRepository;
import com.arif.gym_management.Repository.UserRepository;
import com.arif.gym_management.helper.AppConstants;
import com.arif.gym_management.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;



@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BillsRepository billsRepository;

     @Autowired
    private PasswordEncoder passwordEncoder;

    // method for adding common data to response
    @ModelAttribute
    public void addCommonData(Model model,Principal principal){
        String use= principal.getName();
        //System.out.println(use);
        //get the user using username(Email)
        
       UserEntity user= userRepository.getUserEntityByUserName(use);
       //System.out.println("helllo oooo "+user);
 
       model.addAttribute("user", user);
    }

    @GetMapping("/index")
    public String dashboard(Model model,Principal principal)
    {
        model.addAttribute("title","Admin Dashboard ");
       return "Admin/admin_dashboard";
    }

    // open add form handler
    @GetMapping("/add-member")
    public String dashboard(Model model){
        model.addAttribute("title","Add Member");
        model.addAttribute("member", new MemberEntity());
        return "Admin/add_member";
    }

    // processing add member form
    @PostMapping("/process-member")
    public String processMember(@ModelAttribute MemberEntity member,
    Principal principal, HttpSession session){
        
    try{
        String name=principal.getName();
        UserEntity user=this.userRepository.getUserEntityByUserName(name);
        member.setUser(user);
        user.getMemberEntities().add(member);
        member.setRole("ROLE_MEMBER");
         
        member.setRoleList(List.of(AppConstants.ROLE_MEMBER));

        // set pssword in encryption format
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        this.userRepository.save(user);
         // message for success

         session.setAttribute("message", new Message("Your member is added !!  Add more","success"));
       // System.out.println("data "+member);
    }catch(Exception e){
       e.printStackTrace();
       
       session.setAttribute("message", new Message("Something went wrong !!  Try Again","danger"));
    } 

        return "Admin/add_member";
    }
    
    //per page 5[n]
    //current page=0 page
    @GetMapping("/show-member/{page}")
    public String showMember(@PathVariable("page") Integer page,Model model,Principal principal) {
        model.addAttribute("title","Show Member");
       
        //contact ki list ko bhejni hai 
         String userName=principal.getName();
         UserEntity user=this.userRepository.getUserEntityByUserName(userName);
         
         Pageable pageable=PageRequest.of(page, 5);

         Page<MemberEntity> member=this.memberRepository.findMemberEntitiesByUserEntity(user.getId(),pageable);

       
         model.addAttribute("member", member);
         model.addAttribute("currentPage", page);
         model.addAttribute("totalPages", member.getTotalPages());
        return "Admin/show_member";
    }

    //showing particular member details
    @GetMapping("/{memid}/member")
    public String showMemberDetails(@PathVariable("memid") Integer memid ,
    Model model,Principal principal)
    {
        Optional<MemberEntity> memberOptional=this.memberRepository.findById(memid);
        MemberEntity member=memberOptional.get();
        
        BillsEntity bills = this.billsRepository.findByMemberId(memid);
        // condition for no one can access another one admin details
        String userName=principal.getName();
        UserEntity user=this.userRepository.getUserEntityByUserName(userName);
        if (user.getId() == member.getUser().getId()) {

           
            model.addAttribute("member",member);
            model.addAttribute("bills",bills);
            model.addAttribute("title", member.getName());
        }
        
        return "Admin/member_deatils";
    }

    // delete member handler

    @GetMapping("/delete/{memid}")
    public String deleteMember(@PathVariable("memid") Integer memid,
     Model model,HttpSession session,Principal principal)
    {
      MemberEntity member=this.memberRepository.findById(memid).get();
       
      UserEntity user=this.userRepository.getUserEntityByUserName(principal.getName());
      user.getMemberEntities().remove(member);

      this.userRepository.save(user);
      
      session.setAttribute("message", new Message("Contact deleted succesfully ....","success"));
       return "redirect:/admin/show-member/0"; 
    }

    //open update from handler
    @PostMapping("/update-member/{memid}")
    public String updateForm(@PathVariable("memid") Integer memid,Model model){
        model.addAttribute("title", "Update Member");

        MemberEntity member =this.memberRepository.findById(memid).get();

        model.addAttribute("member", member);
        return "Admin/update_member";
    }

    //update conotact handler
    @PostMapping("/process-update")
    public String upadteHandler(@ModelAttribute MemberEntity member,
    HttpSession session,Principal principal) {
        
       try {
          
        // old contact details
     //  MemberEntity oldMember= this.memberRepository.findById(member.getMemid()).get();
         UserEntity user=this.userRepository.getUserEntityByUserName(principal.getName());
         
         member.setUser(user);
          this.memberRepository.save(member);

          session.setAttribute("message", new Message("Your contact is updated... ", "success"));

       } catch (Exception e) {
        e.printStackTrace();
       }

        return "redirect:/admin/"+member.getMemid()+"/member";
    }
    
    @GetMapping("/admin-profile")
    public String adminProfile(Model model) {

        model.addAttribute("title","Admin Profile");
        return "Admin/admin_profile";
    }
   // add  bills
   @GetMapping("/member-bill/{memid}")
   public String addBills(@PathVariable("memid") Integer memid, Model model) {
       Optional<MemberEntity> memberOptional = this.memberRepository.findById(memid);

       model.addAttribute("title", "Add Bill");
       if (memberOptional.isPresent()) {
           model.addAttribute("memid", memid);
           model.addAttribute("member", memberOptional.get());
       } else {
           model.addAttribute("message", new Message("Member not found.", "danger"));
       }
       model.addAttribute("bill", new BillsEntity());
       return "Admin/add_bills";
   }
   

    // Method to process adding a bill for a specific member
    @PostMapping("/process-bill/{memid}")
public String processBill(@PathVariable("memid") Integer memid, 
                          @ModelAttribute BillsEntity bill, 
                          Principal principal, HttpSession session) {
    try {
        // Fetch the member based on the provided ID
        Optional<MemberEntity> memberOptional = this.memberRepository.findById(memid);

        if (memberOptional.isPresent()) {
            MemberEntity member = memberOptional.get();
            UserEntity user = this.userRepository.getUserEntityByUserName(principal.getName());

            // Verify that the user owns the member to whom the bill is being added
            if (user.getId() == member.getUser().getId()) {

                 // Check if a bill already exists for this member
                 List<BillsEntity> existingBills = this.billsRepository.findByMember(member);
                 if (!existingBills.isEmpty()) {
                     // If a bill already exists, show an error message
                     session.setAttribute("message", new Message("A bill for this member already exists.", "danger"));
                     return "redirect:/admin/show-member/0";
                 }

                // Associate the bill with the member and save
                bill.setMember(member);
                this.billsRepository.save(bill);

                // Feedback for successful bill addition
                session.setAttribute("message", new Message("Bill added successfully!", "success"));
            } else {
                session.setAttribute("message", new Message("You are not authorized to add a bill for this member.", "danger"));
            }
        } else {
            session.setAttribute("message", new Message("Member not found.", "danger"));
        }
    } catch (Exception e) {
        e.printStackTrace();
        session.setAttribute("message", new Message("Something went wrong. Please try again.", "danger"));
    }

    return "redirect:/admin/show-member/0";
}

    @GetMapping("/show-bills/{page}")
    public String showBills(@PathVariable("page") Integer page,Model model,Principal principal) {
             model.addAttribute("title","Show Bills");
           
            //contact ki list ko bhejni hai 
             String userName=principal.getName();
             UserEntity user=this.userRepository.getUserEntityByUserName(userName);

             Pageable pageable = PageRequest.of(page, 5);
             
             Page<BillsEntity> bills = this.billsRepository.findAllBillsByUserId(user.getId(),pageable);
             model.addAttribute("bills", bills.getContent());
             model.addAttribute("currentPage", page);
             model.addAttribute("totalPages", bills.getTotalPages());
            
        return "Admin/show_bills";
    }

    @Transactional
    @GetMapping("/delete-bill/{billid}")
    public String deleteBill(@PathVariable("billid") Integer billid,
     Model model,HttpSession session,Principal principal)
    {
        try {
            // Fetch the bill using the bill ID
            Optional<BillsEntity> billOptional = this.billsRepository.findById(billid);
    
            if (billOptional.isPresent()) {

                BillsEntity bill = billOptional.get();

                // System.out.println("Bill fetched: " + bill.toString());
                MemberEntity member = bill.getMember();
                UserEntity user = this.userRepository.getUserEntityByUserName(principal.getName());
    
                // Check if the user is authorized to delete this bill
                if (user.getId() == member.getUser().getId()) {
                    // Delete the bill
                    if (member != null) {
                        member.getBills().remove(bill); 
                        bill.setMember(null);                       // Update parent-child relationship
                    }
                    this.billsRepository.delete(bill);
    
                    session.setAttribute("message", new Message("Bill deleted successfully!", "success"));
                } else {
                    session.setAttribute("message", new Message("You are not authorized to delete this bill.", "danger"));
                }
            } else {
                session.setAttribute("message", new Message("Bill not found.", "danger"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong. Please try again.", "danger"));
        }
    
        return "redirect:/admin/show-bills/0";
    }

    @PostMapping("/update-bill/{billid}")
    public String updateBill(@PathVariable("billid") Integer billid,Model model){

        model.addAttribute("title", "Update Bill");
        BillsEntity bill = this.billsRepository.findByBillid(billid)
        .orElseThrow(() -> new IllegalArgumentException("Bill not found with ID: " + billid));
        

        model.addAttribute("bill", bill);
        return "Admin/update_bill";
    }
    
    // Method to process the update of a bill
@PostMapping("/process-update-bill/{billid}")
public String processUpdateBill(@PathVariable("billid") Integer billid,
                                @ModelAttribute BillsEntity bill,
                                Principal principal, HttpSession session) {
    try {
        // Fetch the current user
        String userName = principal.getName();
        UserEntity user = this.userRepository.getUserEntityByUserName(userName);

        // Fetch the bill from the database using its ID
        BillsEntity existingBill = this.billsRepository.findById(billid)
            .orElseThrow(() -> new IllegalArgumentException("Bill not found with ID: " + billid));

        // Verify that the user is authorized to update the bill
        if (user.getId() != existingBill.getMember().getUser().getId()) {
            session.setAttribute("message", new Message("You are not authorized to update this bill.", "danger"));
            return "redirect:/admin/show-bills/0";
        }

        // Update the existing bill's fields with the new data
        existingBill.setName(bill.getName());
        existingBill.setEmail(bill.getEmail());
        existingBill.setAmount(bill.getAmount());
        existingBill.setDuration(bill.getDuration());
        existingBill.setBillingDate(bill.getBillingDate());
        existingBill.setStartingDate(bill.getStartingDate());
        existingBill.setEndDate(bill.getEndDate());
        existingBill.setPaid(bill.getPaid());

        // Save the updated bill back to the repository
        this.billsRepository.save(existingBill);

        // Add a success message to the session
        session.setAttribute("message", new Message("Bill updated successfully!", "success"));

        
    } catch (Exception e) {
        e.printStackTrace();
        session.setAttribute("message", new Message("Something went wrong. Please try again.", "danger"));
    }

    return "redirect:/admin/show-bills/0";
}
//showing particular bill details
@GetMapping("/{billid}/bill")
public String showBillDetails(@PathVariable("billid") Integer billid,
                               Model model, Principal principal, HttpSession session) {
    try {
        // Fetch the bill using the bill ID
        Optional<BillsEntity> billOptional = this.billsRepository.findById(billid);

        if (billOptional.isPresent()) {
            BillsEntity bill = billOptional.get();
            UserEntity user = this.userRepository.getUserEntityByUserName(principal.getName());

            // Check if the user is authorized to view this bill
            if (user.getId() == bill.getMember().getUser().getId()) {
                // Add bill details to the model
                model.addAttribute("title", "Bill Details");
                model.addAttribute("bill", bill);
                return "Admin/bill_detail";
            } else {
                session.setAttribute("message", new Message("You are not authorized to view this bill.", "danger"));
            }
        } else {
            session.setAttribute("message", new Message("Bill not found.", "danger"));
        }
    } catch (Exception e) {
        e.printStackTrace();
        session.setAttribute("message", new Message("Something went wrong. Please try again.", "danger"));
    }

    // Redirect back to the list of bills if any issue occurs
    return "redirect:/admin/show-bills/0";
}


}
