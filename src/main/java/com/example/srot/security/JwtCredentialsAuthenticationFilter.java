package com.example.srot.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.srot.business.domain.DefaultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.srot.business.domain.DefaultResponse.Status.FAILED;
import static com.example.srot.business.domain.DefaultResponse.Status.SUCCESS;

public class JwtCredentialsAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final SecretHolder secretHolder;
    private final AuthenticationManager authenticationManager;

    public JwtCredentialsAuthenticationFilter(SecretHolder secretHolder, AuthenticationManager authenticationManager) {
        this.secretHolder = secretHolder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            CredentialsAuthenticationRequest authenticationRequest =
                    new ObjectMapper().readValue(request.getInputStream(), CredentialsAuthenticationRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        Algorithm algorithm = Algorithm.HMAC256(secretHolder.getSecret());
        String access_token = JWT.create()
                .withSubject(authResult.getName())
                .withClaim("roles",authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withIssuer("srot")
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.valueOf(LocalDate.now().plusDays(1)))
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(authResult.getName())
                .withClaim("authorities",authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withIssuer("srot")
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.valueOf(LocalDateTime.now().plusWeeks(2).toLocalDate()))
                .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token",access_token);
        Cookie refreshTokenCookie = new Cookie("refresh_token",refresh_token);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60*60*24*7 ); // 7days expiry for refresh_token cookie
        refreshTokenCookie.setDomain("srot.io");
//        refreshTokenCookie.setDomain(secretHolder.getDomain()); //TODO this line should be uncommented on production code
        response.addCookie(refreshTokenCookie);
        DefaultResponse responseBody = new DefaultResponse(SUCCESS, tokens,
                "Auth JWT generated successfully"
        );
        new ObjectMapper().writeValue(response.getOutputStream(),responseBody);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(403);
        DefaultResponse responseBody = new DefaultResponse(FAILED,"Invalid username or password" );
        new ObjectMapper().writeValue(response.getOutputStream(),responseBody);
    }
}
