package com.example.srot.security;

import com.example.srot.business.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.example.srot.data.enums.Role.CONSUMER;
import static com.example.srot.data.enums.Role.INVESTOR;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authService;
    private final SecretHolder secretHolder;

    @Autowired
    public SecurityConfigurations(PasswordEncoder passwordEncoder, AuthenticationService authService, SecretHolder secretHolder) {
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
        this.secretHolder = secretHolder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
        //        .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        //        .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors()
                .and()
                .addFilterBefore(new JwtAuthorizationFilter(secretHolder), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JwtCredentialsAuthenticationFilter(secretHolder,authenticationManager()))
                .authorizeRequests()
                .antMatchers("/signup/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/actuator/health").permitAll()
                .antMatchers("/email/verify").permitAll()
                .antMatchers("/investor/forget_password").permitAll()
                .antMatchers("/referral").hasRole(INVESTOR.name())
                .antMatchers("/investor/**").hasRole(INVESTOR.name())
                .antMatchers("/assets/**").hasAnyRole(INVESTOR.name())
                .antMatchers("/sign/**").hasAnyRole(INVESTOR.name())
                .antMatchers("/**").hasAnyRole(INVESTOR.name(), CONSUMER.name())
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(new SrotAccessDeniedHandler())
                .and()
                .logout()
                .addLogoutHandler(new SrotLogoutHandler())
                .invalidateHttpSession(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(authService);
        return provider;
    }

}
