package com.example.spring.security.config;

import com.example.spring.security.exceptionhandling.CustomAccessDeniedHandler;
import com.example.spring.security.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.example.spring.security.filter.CsrfCookiesFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement(smc -> smc
                        .invalidSessionUrl("/invalid-session")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
//                        .expiredUrl("/expired")
                )
//                .requiresChannel(rcc -> rcc.anyRequest().requiresSecure())
                .csrf(csrfConfig -> csrfConfig.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookiesFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((request) -> request
//                        .requestMatchers(HttpMethod.GET, "/home").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/register").permitAll()
                        .anyRequest().authenticated()
                );
        httpSecurity.formLogin(Customizer.withDefaults());
        httpSecurity.httpBasic(hbc ->
                hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        httpSecurity.exceptionHandling(ehc ->
                ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return httpSecurity.build();
    }


//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withUsername("user").password("{noop}12345").authorities("read").build();
//        UserDetails admin = User.withUsername("admin")
//                .password("{bcrypt}$2a$12$tKGFZijxdqHj7eAxEdpDSuHpdJ2tNYMEcTaz3.4RU4FH9S7N0hDLy").authorities("admin").build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }

//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource) {
//        return new JdbcUserDetailsManager(dataSource);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
