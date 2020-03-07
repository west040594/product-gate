package com.tstu.productgate.controller.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.tstu.commons.dto.http.request.View;
import com.tstu.commons.dto.http.request.productinfo.CategoryDataRequest;
import com.tstu.commons.dto.http.response.productinfo.CategoryResponse;
import com.tstu.productgate.components.RequestContext;
import com.tstu.productgate.exception.ProductGateExceptionMessage;
import com.tstu.productgate.feign.productinfo.ProductInfoService;
import com.tstu.productgate.service.GateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/gate/product-info/categories")
public class CategoryRestController {

    private final GateService gateService;
    private final ProductInfoService productInfoService;
    private final RequestContext requestContext;

    /**
     * Создание(добавление) новой категории
     * @param token Токен пользователя полученный из authentication-service (обязательный к заполнению)
     * @param categoryDataRequest Запрос на добавление новой категории
     * @return Созданная категория
     */
    @PostMapping("/create")
    @ApiOperation(value = "${api.swagger.category.create}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 403, message = ProductGateExceptionMessage.ACCESS_DENIED_MSG),
            @ApiResponse(code = 422, message = ProductGateExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<?> createCategory(
            @RequestHeader("Authorization") String token,
            @JsonView(View.PRODUCT_GATE.class) @RequestBody @Valid CategoryDataRequest categoryDataRequest) {
        requestContext.setToken(token);
        log.info("Запрос - {}", categoryDataRequest);
        CategoryResponse categoryResponse = gateService.processCreateCategory(categoryDataRequest);
        return ResponseEntity.ok(categoryResponse);
    }


    /**
     * Получение конкретногй категории по его id
     * @param id Id категории
     * @return Структура ответа с найденной категорией
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "${api.swagger.category.id}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 422, message = ProductGateExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        CategoryResponse categoryResponse = productInfoService.getCategoryById(id);
        return ResponseEntity.ok(categoryResponse);
    }

    /**
     * Получение конкретногй категории по её наименованию
     * @param name Наименование категории
     * @return Структура ответа с найденной категорией
     */
    @GetMapping("name/{name}")
    @ApiOperation(value = "${api.swagger.category.name}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 403, message = ProductGateExceptionMessage.ACCESS_DENIED_MSG),
            @ApiResponse(code = 422, message = ProductGateExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<?> getCategoryByName(@PathVariable String name) {
        CategoryResponse categoryResponse = productInfoService.getCategoryByName(name);
        return ResponseEntity.ok(categoryResponse);
    }

    /**
     * Получение всех возможных категорий
     * @return Структура ответа со списком категорий
     */
    @GetMapping
    @ApiOperation(value = "${api.swagger.category.all}")
    public ResponseEntity<?> getAllCategories() {
        List<CategoryResponse> categories = productInfoService.getAllCategories();
        return ResponseEntity.ok(categories);
    }


    /**
     * Запрос на удаление категории
     * @param id Id категории
     * @return Структура ответа с информацией, что запрос на удаление был произведен
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "${api.swagger.category.delete}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 403, message = ProductGateExceptionMessage.ACCESS_DENIED_MSG),
            @ApiResponse(code = 422, message = ProductGateExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<?> deleteCategoryById(@PathVariable("id") Long id) {
        productInfoService.deleteCategoryById(id);
        return ResponseEntity.ok("Запись удалена");
    }
}
