package com.tstu.productgate.controller.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@ApiIgnore
public class MainController {

    @GetMapping(value = {"/home", "/"})
    public String homePage(Model model) {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("greentings", "Главная");
        model.addAttribute("data", data);
        return "home";
    }

}