package com.tstu.productgate.controller.ui;

import com.tstu.commons.dto.http.request.productgate.forms.NewProductForm;
import com.tstu.commons.dto.http.request.productinfo.ProductDataRequest;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tstu.commons.constants.NetworkModelTypes.ENERGY_DRINKS;

@Controller
@ApiIgnore
@RequiredArgsConstructor
@RequestMapping("/products")
@Slf4j
public class ProductController {

    private final ProductInfoService productInfoService;
    private final ProductDeterminationService productDeterminationService;
    private final GateService gateService;
    private final RequestContext requestContext;
    private final ModelMapper modelMapper;


    @GetMapping
    public String productListPage(@RequestParam(name = "category") Optional<String> categoryAlias, Model model) {
        HashMap<Object, Object> data = new HashMap<>();
        model.addAttribute("data", data);
        List<ProductResponse> products = categoryAlias.isPresent() ? productInfoService.getProductsByCategoryAlias(categoryAlias.get())
                : productInfoService.getAllProducts();
        data.put("products", products);
        List<CategoryResponse> categories = productInfoService.getAllCategories();
        data.put("categories", categories);
        model.addAttribute("data", data);
        return "productList";
    }

    @GetMapping("/{id}")
    public String productPage(@PathVariable("id") Long id, Model model) {
        HashMap<Object, Object> data = new HashMap<>();
        model.addAttribute("data", data);
        ProductResponse productById = productInfoService.getProductById(id);
        data.put("product", productById);
        model.addAttribute("data", data);
        return "product";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Model model) {
        productInfoService.deleteProductById(id);
        return "redirect:/home";
    }

    @GetMapping("/create")
    public String createForm(@RequestParam(name = "category") Optional<String> categoryAlias, Model model) {
        //todo сделать категорию вместо константного значения
        List<String> classLabels = productDeterminationService.getClassLabels(categoryAlias.orElse(ENERGY_DRINKS));
        List<CategoryResponse> categories = productInfoService.getAllCategories();
        HashMap<Object, Object> data = new HashMap<>();
        data.put("names", classLabels);
        data.put("categories", categories);
        model.addAttribute("data", data);
        model.addAttribute("productForm", new NewProductForm());
        return "productCreate";
    }

    @PostMapping("/create")
    public String processCreate(@ModelAttribute("productForm") @Valid NewProductForm productForm,
                                BindingResult result, Model model, @AuthenticationPrincipal User user) {
        if(result.hasErrors()) {
            return "productCreate";
        } else {
            requestContext.setToken(user.getJwtToken());
            ProductResponse product = gateService.processCreateProduct(modelMapper.map(productForm, ProductDataRequest.class));
            Long productId = product.getId();
            return "redirect:/products/" + productId;
        }
    }


}
