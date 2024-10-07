package com.spring.boot.smartercontactmanager.controller;

import java.security.Principal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.boot.smartercontactmanager.entites.Contact;
import com.spring.boot.smartercontactmanager.entites.User;
import com.spring.boot.smartercontactmanager.helper.Message;
import com.spring.boot.smartercontactmanager.repository.ContactsRepository;
import com.spring.boot.smartercontactmanager.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactsRepository contactsRepository;

    @GetMapping("/dashboard")
    public String userDashboard(Model model,Principal principal){
          model.addAttribute("title","User Dashboard-");
          String name = principal.getName();
          model.addAttribute("name", name);
          System.out.println("name:"+name);
          User user = this.userRepository.getUserByName(name);
          model.addAttribute("user", user);
          System.out.println(user);
          return "user/dashboard";
    }

    @GetMapping("/addForm")
    public String addFormData(Principal principal ,Model model){
        model.addAttribute("contact", new Contact());
        if (principal != null) {
            String name = principal.getName();
            model.addAttribute("name", name);
        }

        return "user/addForm";
    }

    @PostMapping("/saveContact")
    public String contactFormData(@ModelAttribute("contact") Contact contact, BindingResult result ,Principal principal,
                          HttpSession session ){
       try {

        if(result.hasErrors()){
           throw new Exception("name and number field id null");
        }
        
        // if(file.isEmpty()){
        //   System.out.println("image is null");
        // }
        // else{
        //     File saveFile = new ClassPathResource("/static/img").getFile();
        //     Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
        //      Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        //     System.out.println("image is upload successfully");
        // }


        String name =  principal.getName();
        User user = this.userRepository.getUserByName(name);
        contact.setUser(user);
        user.getContacts().add(contact);
        this.userRepository.save(user);
         System.out.println("Contact:"+contact);

         session.setAttribute("error",new Message("Store Successfully","alert-success") );


       } catch (Exception e) {
           e.printStackTrace();
           session.setAttribute("error",new Message("Some think want Wrong!! Please Try Again", "alert-danger"));
       }
        return "user/addForm";
    }


    @GetMapping("/contacts/{page}")
    public String getContacts(Model m ,Principal principal,
    @PathVariable("page") Integer page)
    {
      if(principal != null){
        String name = principal.getName();
        m.addAttribute("name", name);
      }
      String name = principal.getName();
      User user =  this.userRepository.getUserByName(name);
      Pageable pageable = PageRequest.of(page, 4); // 5 contacts per page
      Page<Contact> contacts = contactsRepository.getDataFromUserId(user.getId(), pageable);
      
      m.addAttribute("contacts", contacts);

      m.addAttribute("currentPage", page);
      m.addAttribute("totalPage", contacts.getTotalPages());

      return "user/show_contacts";
    }


    @GetMapping("/{cId}/contacts")
    public String getProfile(@PathVariable("cId") Integer id,Model model,Principal principal ){
 

      if(principal != null){
        String name = principal.getName();
        model.addAttribute("name", name);
      }
 
    String name = principal.getName();
    User user =   this.userRepository.getUserByName(name);
       Optional<Contact> contact = this.contactsRepository.findById(id);
       Contact cont = contact.get();
       

       if(user.getId() == cont.getUser().getId())
       {
          model.addAttribute("contact", cont);
       }
      return "user/showProfile";
    }

    @GetMapping("/delete/{id}")
    public String deleteData(@PathVariable("id") int id,Model model,HttpSession session){
        Contact contact =  this.contactsRepository.findById(id).get();
        contact.setUser(null);
        this.contactsRepository.delete(contact);
        session.setAttribute("message", new Message("Data Delete Successfully","alert-success"));
        return "redirect:/user/contacts/0";
    }


    @GetMapping("/update/{id}")
    public String updateData(@PathVariable("id") int id ,Model model){
           Contact contact = this.contactsRepository.findById(id).get();
           if(contact != null){
             model.addAttribute("contact", contact);
           }
           return "/user/update_form";
    }


    @PostMapping("/updateForm")
    public String updateFormData(@ModelAttribute("contact") Contact contact,Principal principal,Model model ){
    
      if(principal != null){
        String name = principal.getName();
        model.addAttribute("name", name);
      }
    User user =  this.userRepository.getUserByName(principal.getName());
    contact.setUser(user);
    this.contactsRepository.save(contact);

    System.out.println("Id"+contact.getcId());
    System.out.println("Name"+contact.getcName());
      return "redirect:/user/contacts/0";
    }


    @GetMapping("/userProfile")
    public String getUserProfile(@ModelAttribute User user,Model model,Principal principal ){
      if(principal != null){
        String name = principal.getName();
        model.addAttribute("name", name);
      }
      String name = principal.getName();
     User user2= this.userRepository.getUserByName(name);
      model.addAttribute("user", user2);
      return "user/userProfile";
    }


     @GetMapping("/search")
    public void searchData(@RequestParam("search") String keyword,Model model){
         List<Contact> search =  this.contactsRepository.searchDataByName(keyword);
         model.addAttribute("query", search);
         model.addAttribute("keyword", keyword);
    }

    @GetMapping("/setting")
    public String setting(Principal principal,Model model){
      if(principal != null){
        String name = principal.getName();
        model.addAttribute("name", name);
      }
      return "user/setting";
    }


    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                          @RequestParam("newPassword") String newPassword,
                         Principal principal,HttpSession session ){
 
      String name =  principal.getName();
      User Curruser =  this.userRepository.getUserByName(name); 
      if(this.bCryptPasswordEncoder.matches(oldPassword,Curruser.getPassword())){
         Curruser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
         this.userRepository.save(Curruser);
         session.setAttribute("message", new Message("password change sucessfully","alert-success"));
      }  
      else{
            session.setAttribute("message",new Message("SomeThing Wrong Please Try Again!","alert-danger"));
            return "redirect:/user/setting";
      }                   
    
      return "redirect:/user/dashboard";
    }
}
