package com.arif.gym_management.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.arif.gym_management.Entities.UserEntity;
//import com.arif.gym_management.Repository.UserRepository;
import com.arif.gym_management.Service.UserService;
import com.arif.gym_management.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller

public class HomeController {

    @Autowired
    private UserService userService;

    // @Autowired
    // private UserRepository userRepository;
    
    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("title","Home - Gym-Management");
        return "home";
    }
    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("title","About - Gym-Management");
        return "about";
    }
    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title","SignUp - Gym-Management");
        model.addAttribute("user", new UserEntity());
        return "SignUpAdmin";
    }

    // @GetMapping("/authenticate")
    // public String authenticate() {
    //     return "User_dashboard"; // Or return the correct view name.
    // }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login -Gym-Management");
        return new String("login");
    } 
    @PostMapping("/login")
    public String Login(Model model) {
      model.addAttribute("title", "Login -Gym-Management");
        return new String("login");
    } 

    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") UserEntity user,BindingResult result, 
    @RequestParam (value="agreement" , defaultValue ="false") 
    boolean agreement ,Model model ,HttpSession session)
     {
      try {  
        if (!agreement) {
            System.out.println("You have not agreed the terms and conditions ");
            throw new Exception("You have not agreed the terms and conditions");
        } 
        if (result.hasErrors()) {
          System.out.println("ERROR"+ result.toString());
          model.addAttribute("user", user);
          return "SignUpAdmin";
        }

        user.setRole("ROLE_ADMIN");
        System.out.println(agreement);
        System.out.println(user);

        // this is used for saving the deatils into databases
        this.userService.saveUser(user);

        model.addAttribute("user", new UserEntity());
        session.setAttribute("message",new Message("Successfully Registerd !!", "alert-success"));
        return "redirect:/signup" ;

      } catch (Exception e){
        e.printStackTrace();
        model.addAttribute("user", user);
        session.setAttribute("message", new Message("Something went wrong !!"+e.getMessage(),"alert-danger") );
        return "SignUpAdmin" ;
      }

    }
    
}
