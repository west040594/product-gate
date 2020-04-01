package com.tstu.productgate.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.tstu.commons.dto.http.request.View;
import com.tstu.commons.dto.http.request.productinfo.ProductDataRequest;
import com.tstu.commons.dto.http.response.productinfo.ProductResponse;
import com.tstu.productgate.components.RequestContext;
import com.tstu.productgate.exception.ProductGateExceptionMessage;
import com.tstu.productgate.feign.productinfo.ProductInfoService;
import com.tstu.productgate.service.GateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/gate/product-info/products")
public class ProductRestController {

    private final GateService gateService;
    private final ProductInfoService productInfoService;
    private final RequestContext requestContext;

    /**
     * Получение списка всех возможножных продуктов
     * @return Список продуктов
     */
    @GetMapping
    @ApiOperation(value = "${api.swagger.product.all}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 422, message = ProductGateExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<?> getAllProducts() {
        List<ProductResponse> allProducts = productInfoService.getAllProducts();
        return ResponseEntity.ok(allProducts);
    }

    /**
     * Получение списка всех продуктов по названию категории
     * @return Список продуктов
     */
    @GetMapping("/category/{categoryName}")
    @ApiOperation(value = "${api.swagger.product.category.name}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 422, message = ProductGateExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<?> getProductsByCategoryName(@PathVariable("categoryName") String categoryName) {
        List<ProductResponse> allProducts = productInfoService.getProductsByCategoryName(categoryName);
        return ResponseEntity.ok(allProducts);
    }


    /**
     * Получение списка всех продуктов по псевдониму категории
     * @return Список продуктов
     */
    @GetMapping("/category/alias/{categoryAlias}")
    @ApiOperation(value = "${api.swagger.product.category.alias}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 422, message = ProductGateExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<?> getProductsByCategoryAlias(@PathVariable("categoryAlias") String categoryAlias) {
        List<ProductResponse> allProducts = productInfoService.getProductsByCategoryAlias(categoryAlias);
        return ResponseEntity.ok(allProducts);
    }

    /**
     * Получение конкретного продукта по его id
     * @param id Id продукта
     * @return Продукт
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "${api.swagger.product.id}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 422, message = ProductGateExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        ProductResponse productById = productInfoService.getProductById(id);
        return ResponseEntity.ok(productById);
    }

    /**
     * Создание(добавление) нового продукта
     * @param token Токен пользователя полученный из authentication-service (обязательный к заполнению)
     * @param productDataRequest Запрос на добавление нового продукта
     * @return Созданный продукт
     */
    @PostMapping("/create")
    @ApiOperation(value = "${api.swagger.product.create}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 403, message = ProductGateExceptionMessage.ACCESS_DENIED_MSG),
            @ApiResponse(code = 422, message = ProductGateExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<?> createProduct(
            @RequestHeader("Authorization") String token,
            @JsonView(View.PRODUCT_GATE.class) @RequestBody @Valid ProductDataRequest productDataRequest) {
        requestContext.setToken(token);
        log.info("Запрос - {}", productDataRequest);
        ProductResponse productResponse = gateService.processCreateProduct(productDataRequest);
        return ResponseEntity.ok(productResponse);
    }

    @PostMapping("/parse")
    @ApiOperation(value = "${api.swagger.product.parse}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 422, message = ProductGateExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<String> sendDomParserRequest(@RequestBody Long productId) {
        String response = productInfoService.sendDomParserRequest(productId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }


    /**
     * Запрос на удаление записи о продукте
     * @param id Id продукта
     * @return Структура ответа с информацией, что запрос на удаление был произведен
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "${api.swagger.product.delete}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 422, message = ProductGateExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        productInfoService.deleteProductById(id);
        return ResponseEntity.ok("Запись удалена");
    }
}
