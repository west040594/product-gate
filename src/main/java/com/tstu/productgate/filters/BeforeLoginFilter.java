package com.tstu.productgate.filters;

import com.tstu.commons.dto.http.request.authenticationsystem.AuthenticationRequest;
import com.tstu.commons.exception.PrsHttpException;
import com.tstu.productgate.feign.autentication.AuthenticationService;
import com.tstu.productgate.util.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class BeforeLoginFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationService authenticationService;

    public BeforeLoginFilter() {
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        this.authenticationService = ServiceUtils.getAuthenticationService();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            authenticationService.signIn(new AuthenticationRequest(username, password));
        } catch (PrsHttpException ex) {
            throw new BadCredentialsException(ex.getMessage());
        }
        return super.attemptAuthentication(request, response);
    }
}
