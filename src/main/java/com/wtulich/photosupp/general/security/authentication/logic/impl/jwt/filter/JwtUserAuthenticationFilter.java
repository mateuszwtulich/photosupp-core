package com.wtulich.photosupp.general.security.authentication.logic.impl.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtulich.photosupp.general.security.authentication.logic.impl.to.AuthenticationPayload;
import com.wtulich.photosupp.general.security.authentication.logic.impl.to.UserAuthenticationRequest;
import com.wtulich.photosupp.general.security.authentication.logic.impl.usecase.UcLoginImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class JwtUserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private UcLoginImpl ucLogin;

    @Autowired
    public JwtUserAuthenticationFilter(AuthenticationManager authenticationManager, UcLoginImpl ucLogin) {
        this.authenticationManager = authenticationManager;
        this.ucLogin = ucLogin;
        this.setFilterProcessesUrl("/api/authenticate");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            UserAuthenticationRequest userAuthenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UserAuthenticationRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userAuthenticationRequest.getUsername(),
                    userAuthenticationRequest.getPassword(),
                    new ArrayList<>());

            Authentication authenticate = authenticationManager.authenticate(authentication);
            return authenticate;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        Long userId = ucLogin.getUserIdByUserName(authResult.getName());
        String token = Jwts.builder()
                .setSubject(authResult.getName()).claim("authorities", authResult.getAuthorities()).claim("userId", userId)
//                .claim("payload", getPayLoad(userId)).setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                //TODO: Change for secured Password and move this password to application.properties
                .signWith(Keys.hmacShaKeyFor("SecuredpASSWOedpASSWORDSecuedpASredpASSWORDSecuredpASSWORD".getBytes())).compact();

        response.addHeader("access-control-expose-headers", "Authorization");
        response.addHeader("Authorization", "Bearer " + token);
    }

//    private AuthenticationPayload getPayLoad(Long userId) {
//        return this.ucLogin.getAuthenticationPayload(userId);
//    }
}
