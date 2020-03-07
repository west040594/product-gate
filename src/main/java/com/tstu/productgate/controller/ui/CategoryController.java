package com.tstu.productgate.controller.ui;

import com.tstu.commons.dto.http.request.productgate.forms.NewCategoryForm;
import com.tstu.commons.dto.http.request.productinfo.CategoryDataRequest;
import com.tstu.commons.dto.http.response.productinfo.CategoryResponse;
import com.tstu.commons.dto.http.response.productinfo.ProductResponse;
import com.tstu.productgate.components.RequestContext;
import com.tstu.productgate.feign.determination.ProductDeterminationService;
import com.tstu.productgate.feign.productinfo.ProductInfoService;
import com.tstu.productgate.models.User;
import com.tstu.productgate.service.GateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Controller
@ApiIgnore
@RequiredArgsConstructor
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

    private final ProductInfoService productInfoService;
    private final ProductDeterminationService productDeterminationService;
    private final GateService gateService;
    private final RequestContext requestContext;
    private final ModelMapper modelMapper;

    @GetMapping
    public String categoryListPage(Model model) {
        HashMap<Object, Object> data = new HashMap<>();
        model.addAttribute("data", data);
        List<CategoryResponse> categories = productInfoService.getAllCategories();
        data.put("categories", categories);
        model.addAttribute("data", data);
        return "categoryList";
    }

    @GetMapping("/{id}")
    public String categoryPage(@PathVariable("id") Long id, Model model) {
        HashMap<Object, Object> data = new HashMap<>();
        model.addAttribute("data", data);
        CategoryResponse categoryResponse = productInfoService.getCategoryById(id);
        data.put("category", categoryResponse);
        model.addAttribute("data", data);
        return "category";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        Set<String> aliases = productDeterminationService.getAliases();
        HashMap<Object, Object> data = new HashMap<>();
        data.put("aliases", aliases);
        model.addAttribute("data", data);
        model.addAttribute("categoryForm", new NewCategoryForm());
        return "categoryCreate";
    }

    @PostMapping("/create")
    public String processCreate(@ModelAttribute("categoryForm") @Valid NewCategoryForm categoryForm,
                                BindingResult result, Model model, @AuthenticationPrincipal User user) {
        if(result.hasErrors()) {
            return "categoryCreate";
        } else {
            requestContext.setToken(user.getJwtToken());
            CategoryResponse category = gateService.processCreateCategory(modelMapper.map(categoryForm, CategoryDataRequest.class));
            Long categoryId = category.getId();
            return "redirect:/categories/" + categoryId;
        }
    }
}
