package com.tstu.productgate.controller.rest;

import com.tstu.commons.dto.http.response.productinfo.ProductResponse;
import com.tstu.productgate.exception.ProductGateExceptionMessage;
import com.tstu.productgate.feign.determination.ProductDeterminationService;
import com.tstu.productgate.service.GateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/gate/product-determination")
public class DeterminationRestController {

    private final GateService gateService;
    private final ProductDeterminationService productDeterminationService;

    /** Предсказание продукта по изображению
     * @param file Файл(Изображение) продукта
     * @return Коллекция продуктов наиболее подходящие под данное изображение
     */
    @PostMapping("/predict/{modelName}")
    @ApiOperation(value = "${api.swagger.determination.predict}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 422, message = ProductGateExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<?> predictImage(@RequestParam("file") MultipartFile file, @PathVariable("modelName") String modelName) {
        log.info("Predict");
        List<ProductResponse> productsByImage = gateService.getProductsByImage(file, modelName);
        return ResponseEntity.ok(productsByImage);
    }

    /**
     * Получение списка всех возможножных продуктов
     * @return Список продуктов
     */
    @GetMapping("/predict/labels/{modelName}")
    @ApiOperation(value = "${api.swagger.determination.labels}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 422, message = ProductGateExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<?> getAllPredictLabels(@PathVariable("modelName") String modelName) {
        List<String> labels = productDeterminationService.getClassLabels(modelName);
        return ResponseEntity.ok(labels);
    }

    @PostMapping(value = "/train/{modelName}")
    @ApiOperation(value = "${api.swagger.determination.train}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
            @ApiResponse(code = 422, message = ProductGateExceptionMessage.UNABLE_TO_PROCESS_DATA),
    })
    public ResponseEntity<String> trainModel(@PathVariable("modelName") String modelName) {
        String response = productDeterminationService.trainModel(modelName);
        return ResponseEntity.ok(response);
    }
}
