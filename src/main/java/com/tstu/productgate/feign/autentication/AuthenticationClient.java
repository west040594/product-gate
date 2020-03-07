package com.tstu.productgate.feign.autentication;

import com.tstu.commons.dto.http.request.authenticationsystem.AuthenticationRequest;
import com.tstu.commons.dto.http.request.authenticationsystem.UserDataRequest;
import com.tstu.commons.dto.http.response.authenticationsystem.AuthenticationResponse;
import com.tstu.commons.dto.http.response.authenticationsystem.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "authentication-system", url = "${feign.services.authentication.url}")
public interface AuthenticationClient {
    @PostMapping("/signin")
    ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authRequest);

    @PostMapping("/signup")
    ResponseEntity<AuthenticationResponse> sigup(@RequestBody UserDataRequest userDataRequest);


    @GetMapping("/me/{token}")
    ResponseEntity<UserResponse> getUserByToken(@PathVariable("token") String token);
}
