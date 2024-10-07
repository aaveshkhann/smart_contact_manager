package com.spring.boot.smartercontactmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class UserConfig {
    
    @Bean
    public UserDetailService getUserDetailService(){
        return new UserDetailService();
    }
    @Bean
    public BCryptPasswordEncoder ePasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

     @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getUserDetailService());
        daoAuthenticationProvider.setPasswordEncoder(ePasswordEncoder());

        return daoAuthenticationProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login").permitAll()
                .requestMatchers("/user/**").authenticated() 
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/loginUrl")
                .defaultSuccessUrl("/user/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
            .logoutUrl("/logout") // URL to trigger logout
            .logoutSuccessUrl("/") // Redirect to "/" after logout
            .permitAll()
        )
        // Handle access denied exceptions
        .exceptionHandling(exception -> exception
            .accessDeniedPage("/403")
        )
        ;
        return http.build();
    }
}
    


