package com.spring.boot.smartercontactmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.boot.smartercontactmanager.entites.User;
import com.spring.boot.smartercontactmanager.helper.Message;
import com.spring.boot.smartercontactmanager.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

   @Autowired
   private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
     
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("home","Smarter Contact Manager");
          return "home";
    }

    @GetMapping("/login")
    public String loginpage(){
          return "login";
    }

    @GetMapping("/signup")
    public String signUpPage(Model model){
        model.addAttribute("user", new User());
          return "signup";
    }

    @PostMapping("/storeformdata")
    public String saveData( @Valid @ModelAttribute("user") User user ,BindingResult result,
         @RequestParam(value = "agreement",defaultValue="false") Boolean agree,
         
         Model model,HttpSession session){
      
      try {
        if(!agree){
            System.out.println("not agree");
            throw new Exception("you are not agreed terms and condition");
         }

         if(result.hasErrors()){
          System.out.println("error");
          return "signup";
         }
         
        user.setRole("User_Role");
        user.setEnabled(true);
        user.setImageUrl("image.jpg");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user);
        System.out.println("agree: "+agree);
        this.userRepository.save(user);
        session.setAttribute("Message", new Message("Successfully register","alert-success"));
        return "signup"; 

      } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("user",user);
        session.setAttribute("Message", new Message("Something want Wrong"+e.getMessage(),"alert-danger"));
        return "signup";   
    }
       
    }
}
