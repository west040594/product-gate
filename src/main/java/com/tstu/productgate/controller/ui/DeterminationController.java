package com.tstu.productgate.controller.ui;


import com.tstu.commons.dto.http.request.productgate.forms.NewCategoryForm;
import com.tstu.commons.dto.http.response.productinfo.CategoryResponse;
import com.tstu.productgate.feign.productinfo.ProductInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Controller
@ApiIgnore
@RequiredArgsConstructor
@RequestMapping("/determination")
@Slf4j
public class DeterminationController {

    private final ProductInfoService productInfoService;


    @GetMapping("/train")
    public String createForm(Model model) {
        List<CategoryResponse> categories = productInfoService.getAllCategories();
        HashMap<Object, Object> data = new HashMap<>();
        data.put("categories", categories);
        model.addAttribute("data", data);
        return "train";
    }
}
