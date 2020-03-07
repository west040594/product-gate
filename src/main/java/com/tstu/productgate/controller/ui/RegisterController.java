package com.tstu.productgate.controller.ui;

import com.tstu.commons.dto.http.request.authenticationsystem.UserDataRequest;
import com.tstu.commons.dto.http.request.productgate.forms.RegistrationForm;
import com.tstu.productgate.feign.autentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;


import javax.validation.Valid;

@Controller
@ApiIgnore
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final ModelMapper modelMapper;
    private final AuthenticationService authenticationService;

    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("registerForm", new RegistrationForm());
        return "registration";
    }

    @PostMapping
    public String processRegistration(@ModelAttribute("registerForm") @Valid RegistrationForm registerForm, BindingResult result) {
        if(result.hasErrors()) {
            return "registration";
        } else {
            authenticationService.singUp(modelMapper.map(registerForm, UserDataRequest.class));
            return "redirect:/login";
        }
    }
}