package com.spring.boot.smartercontactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.spring.boot.smartercontactmanager.entites.User;
import com.spring.boot.smartercontactmanager.repository.UserRepository;

public class UserDetailService implements UserDetailsService{


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
          User user = this.userRepository.getUserByEmail(email);
          if(user == null){
            throw new UsernameNotFoundException("User not found");
          }

          CustomUserDetail customUserDetail = new CustomUserDetail(user);
        return customUserDetail;
    }  
}
