package com.tstu.productgate.controller.ui;


import com.tstu.commons.dto.http.request.productgate.forms.DoDeterminationForm;
import com.tstu.commons.dto.http.request.productgate.forms.NewCategoryForm;
import com.tstu.commons.dto.http.request.productgate.forms.NewProductForm;
import com.tstu.commons.dto.http.request.productinfo.ProductDataRequest;
import com.tstu.commons.dto.http.response.productinfo.CategoryResponse;
import com.tstu.commons.dto.http.response.productinfo.ProductResponse;
import com.tstu.productgate.feign.productinfo.ProductInfoService;
import com.tstu.productgate.models.User;
import com.tstu.productgate.service.GateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
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
    private final GateService gateService;


    @GetMapping("/train")
    public String trainForm(Model model) {
        List<CategoryResponse> categories = productInfoService.getAllCategories();
        HashMap<Object, Object> data = new HashMap<>();
        data.put("categories", categories);
        model.addAttribute("data", data);
        return "train";
    }


    @GetMapping("/predict")
    public String predictForm(Model model) {
        HashMap<Object, Object> data = new HashMap<>();
        List<CategoryResponse> categories = productInfoService.getAllCategories();
        data.put("categories", categories);
        model.addAttribute("data", data);
        model.addAttribute("determinationForm", new DoDeterminationForm());
        return "determinationProduct";
    }

    @PostMapping(value = "/predict")
    public String processPrediction(@ModelAttribute("determinationForm") @Valid DoDeterminationForm doDeterminationForm,
                               BindingResult result, Model model) throws IOException {
        if(result.hasErrors()) {
            return "determinationProduct";
        } else {
            ProductResponse product = gateService.getSingleProductsByImage(doDeterminationForm.getFile(), doDeterminationForm.getModelName());
            Long productId = product.getId();
            return "redirect:/products/" + productId;
        }
    }

}
