package com.lchhung.jwt.server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    //@Autowired
    private UserPrincipleDetailsService userPrincipleDetailsService;

    public SecurityConfiguration(UserPrincipleDetailsService userPrincipleDetailsService) {
        this.userPrincipleDetailsService = userPrincipleDetailsService;
    }

//    //1. This is to config In Memmory User
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("admin")
//                    .password(passwordEncoder().encode("admin")).roles("ADMIN")
//
//                // When we define .authorities(""ACCESS_TEST1","ACCESS_TEST2","ROLE_ADMIN"),
//                // which means that permission and role can be granted.
//                    .authorities("ACCESS_TEST1","ACCESS_TEST2","ROLE_ADMIN")
//
//                .and()
//
//                .withUser("user")
//                    .password(passwordEncoder().encode("user")).roles("USER")
//
//                .and()
//
//                .withUser("manager")
//                    .password(passwordEncoder().encode("manager"))
//                    .authorities("ACCESS_TEST1","ROLE_MANAGER");
//    }


    //.0 Create a brean for authentification provider

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider (){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipleDetailsService);

        return daoAuthenticationProvider;

    }

    //1. If use user from database

    protected void configure (AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }





    //2. Authorise request
    @Override
    protected void configure (HttpSecurity http) throws Exception {
        http
                .authorizeRequests() // Authorise request
                .antMatchers("/index.html").permitAll() // Permit all request to get access to home page
                //.antMatchers("/profile/index").authenticated() // Alternatively defined route
                .antMatchers("/profile/**").authenticated()

                //We put authorize() to permit specific access because many people would have the same role but different
                //acceses might need to be allow.
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/management/**").hasAnyRole("ADMIN", "MANAGER")

                // This is to protect rest API (resource)
                //.antMatchers("/api/public/**").authenticated()
                //.antMatchers("/api/public/**").hasAnyRole("ADMIN","MANAGER")

                //This is allow ADMIN to get access to this API as define in config above
                .antMatchers("/api/public/test1").hasAuthority("ACCESS_TEST1")

                //This is allow anyone who has authority access to  get access to test2
                .antMatchers("/api/public/test2").hasAuthority("ACCESS_TEST2")

                .antMatchers("/api/public/users").hasRole("ADMIN")
                .and()
                //.formLogin()
                .httpBasic();
                //.loginPage("/login").permitAll();
                //.httpBasic();
    }

    //3. Need to provide a password encoder to hash password to be stored in plain text
    @Bean
    PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }
}
