package com.example.srot.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.srot.business.domain.DefaultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static com.example.srot.business.domain.DefaultResponse.Status.FAILED;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final SecretHolder secretHolder;

    public JwtAuthorizationFilter(SecretHolder secretHolder) {
        this.secretHolder = secretHolder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        if(path.equals("/login")||path.equals("/login/refreshToken")){
            filterChain.doFilter(request,response);
        } else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                try{
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256(secretHolder.getSecret());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(e->{
                        authorities.add(new SimpleGrantedAuthority(e));
                    });
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(new UsernamePasswordAuthenticationToken(username,null,authorities));
                }catch (Exception e){
                    log.error("Error logging in: {}",e.getMessage());
                    response.setStatus(403);
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            new DefaultResponse(FAILED,e.getMessage()));
                    return;
                }
                filterChain.doFilter(request,response);
            }else{
                filterChain.doFilter(request,response);
            }
        }
    }
}
