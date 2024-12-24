package com.arif.gym_management.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.arif.gym_management.Service.CombinedUserDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private CombinedUserDetailsService userDetailService;

   

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    // configuration of authentication provider for spring security 
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // user detail service ka object:
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        // password encoder ka object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.authorizeHttpRequests(authorize ->{
        authorize.requestMatchers("/admin/**").hasRole("ADMIN");
        authorize.requestMatchers("/member/**").hasRole("MEMBER");
          authorize.anyRequest().permitAll();
    }).formLogin(formLogin ->{
        formLogin.loginPage("/login");
       formLogin.loginProcessingUrl("/authenticate");
        //formLogin.successForwardUrl("/");
        formLogin.failureForwardUrl("/login?error=true");
        formLogin.successHandler(customSuccessHandler);
        formLogin.usernameParameter("email");
        formLogin.passwordParameter("password");

      });

          httpSecurity.csrf(AbstractHttpConfigurer::disable);
           httpSecurity.logout(logoutForm -> {
           logoutForm.logoutUrl("/logout");
           logoutForm.logoutSuccessUrl("/login?logout=true");
        });

  
    return httpSecurity.build();
 
}
    
    @Bean
    public PasswordEncoder passwordEncoder() {

//         // password check karne ke liye ki jo save hai galat hai ya shi 
// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
// boolean matches = encoder.matches("123", "$2a$10$UPj473e3zhu8S27KkC0r7bLsXSBpSHCv8Xwj4wca2L9FfdqjSb7u");
// System.out.println("Password matches: " + matches);

//    // password ko encode krne ke liye 
// String encodedPassword = encoder.encode("123");
// System.out.println("Encoded Password: " + encodedPassword);

return new BCryptPasswordEncoder();

    }
    
}
