package com.example.srot.business.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.srot.business.service.exceptions.JWTException;
import com.example.srot.business.service.exceptions.NotFoundException;
import com.example.srot.data.model.Investor;
import com.example.srot.data.model.Profile;
import com.example.srot.data.repository.ConsumerRepository;
import com.example.srot.data.repository.InvestorRepository;
import com.example.srot.security.SecretHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static java.lang.String.format;

@Service
@Slf4j
public class AuthenticationService implements UserDetailsService {

    private final InvestorRepository investorRepository;
    private final ConsumerRepository consumerRepository;
    private final SecretHolder secretHolder;

    @Autowired
    public AuthenticationService(InvestorRepository investorRepository, ConsumerRepository consumerRepository, SecretHolder secretHolder) {
        this.investorRepository = investorRepository;
        this.consumerRepository = consumerRepository;
        this.secretHolder = secretHolder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<? extends Profile> user;
        if(username.contains("@"))user = investorRepository.findByEmail(username);
        else user = investorRepository.findByPhoneNumber(username);
        if(user.isEmpty()){
            if (username.contains("@"))user = consumerRepository.findByEmail(username);
            else user = consumerRepository.findByPhoneNumber(username);
        }
        return user.orElseThrow(()-> new NotFoundException("No user with provided information was found"));
    }

    public String refreshAccessToken(HttpServletRequest request, HttpServletResponse response){


        Cookie[] cookies = request.getCookies();
        if(cookies==null) throw new JWTException("Error refreshing access token: no cookies were found");
        Cookie refreshTokenCookie = null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("refresh_token")) refreshTokenCookie = cookie;
        }
        if (refreshTokenCookie==null) {
            log.error("Error refreshing access token: unable to find 'refresh_token' cookie");
            throw new JWTException("Error refreshing access token: unable to find 'refresh_token' cookie");
        }
        try {
            String refreshToken = refreshTokenCookie.getValue();
            Algorithm algorithm = Algorithm.HMAC256(secretHolder.getSecret());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            String username = decodedJWT.getSubject();
            Profile user = (Profile) loadUserByUsername(username);
            return JWT.create()
                    .withSubject(user.getEmail())
                    .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .withIssuer("srot")
                    .withIssuedAt(new Date())
                    .withExpiresAt(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                    .sign(algorithm);

        } catch (Exception e) {
            log.error("Error refreshing access token: {}", e.getMessage());
            throw new JWTException(format("Error refreshing access token: %s",e.getMessage()));
        }
    }

    public Investor findAuthenticatedInvestor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        return investorRepository.findByEmail(currentUserName).orElseThrow(() ->
                new NotFoundException("User " + currentUserName + " not found"));
    }
}
