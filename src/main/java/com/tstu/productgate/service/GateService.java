package com.tstu.productgate.service;

import com.tstu.commons.dto.http.request.productinfo.CategoryDataRequest;
import com.tstu.commons.dto.http.request.productinfo.ProductDataRequest;
import com.tstu.commons.dto.http.response.authenticationsystem.UserResponse;
import com.tstu.commons.dto.http.response.determination.PredictionResponse;
import com.tstu.commons.dto.http.response.productinfo.CategoryResponse;
import com.tstu.commons.dto.http.response.productinfo.ProductResponse;
import com.tstu.commons.exception.PrsHttpException;
import com.tstu.productgate.exception.ProductGateErrors;
import com.tstu.productgate.feign.autentication.AuthenticationService;
import com.tstu.productgate.feign.determination.ProductDeterminationService;
import com.tstu.productgate.feign.productinfo.ProductInfoService;
import com.tstu.productgate.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GateService {

    private final ProductDeterminationService productDeterminationService;
    private final ProductInfoService productInfoService;
    private final AuthenticationService authenticationService;


    /** Определение продукта по изображению
     * Алгоритм:
     * 1. Определелить наименование продукта по изображению в product-determination-service.
     *  В качестве ответа будет получен список наименований продуктов, наиболее подходяшие под это изображение
     * 2. Передать данные наименования продуктов в product-info-service для получения полной информация по наименованию этих продуктов
     *  В качестве ответа будет получен список продуктов
     * @param file Файл(Изображение) продукта
     * @return Коллекция продуктов наиболее подходящие под входное изображение
     */
    public List<ProductResponse> getProductsByImage(MultipartFile file, String modelName) {
        log.info("Делаем предсказание в {} и получаем список продуктов", modelName);
        PredictionResponse predictionResponse = productDeterminationService.predictImage(file, modelName);
        List<String> productNames = predictionResponse.getProducts().stream().map(PredictionResponse.Item::getName).collect(Collectors.toList());
        return productInfoService.getAllProductsByPredict(productNames);
    }


    /** Определение продукта по изображению
     * Алгоритм:
     * 1. Определелить наименование продукта по изображению в product-determination-service.
     *  В качестве ответа будет получен список наименований продуктов, наиболее подходяшие под это изображение
     * 2. Получить из списка продукт с наибольшим коэфициентом совпадения
     * 3. Сделать поиск продукта по наименованию в product-info-service для получения полной информации о нем
     *  В качестве ответа будет получена детальная информация о продукте
     * @param file Файл(Изображение) продукта
     * @return Продукт наиболее подходящий под входное изображение
     */
    public ProductResponse getSingleProductsByImage(MultipartFile file, String modelName) {
        log.info("Делаем предсказание в {} и получаем продукт", modelName);
        PredictionResponse predictionResponse = productDeterminationService.predictImage(file, modelName);
        String productName = predictionResponse.getProducts().stream()
                .max(Comparator.comparingDouble(PredictionResponse.Item::getRatio))
                .map(PredictionResponse.Item::getName)
                .orElseThrow(() -> new PrsHttpException(ProductGateErrors.COULD_NOT_DETERMINE_PRODUCT_ERROR, HttpStatus.UNPROCESSABLE_ENTITY));
        return productInfoService.getProductByPredict(productName);
    }


    /**
     * Добавление нового продукта в базу product-info
     * Алгоритм:
     * 1.Получить текущего пользователя по полученному токену в authentication-service
     * 2.Добавить его как пользователя, который создал этот продукт
     * 3. Отправить запрос на создание нового продукта в product-info-service
     * @param productDataRequest Запрос на создание нового продукта
     * @return Созданный продукт
     */
    public ProductResponse processCreateProduct(ProductDataRequest productDataRequest) {
        User userByToken = authenticationService.getUserByToken() ;
        productDataRequest.setCreatedBy(userByToken.getUsername());
        return productInfoService.createNewProduct(productDataRequest);
    }


    /**
     * Добавление новой категории в базу product-info
     * Алгоритм:
     * 1.Получить текущего пользователя по полученному токену в authentication-service
     * 3. Отправить запрос на создание новой категории в product-info-service
     * @param categoryDataRequest Запрос на создание нового продукта
     * @return Созданная категория
     */
    public CategoryResponse processCreateCategory(CategoryDataRequest categoryDataRequest) {
        User userByToken = authenticationService.getUserByToken() ;
        return productInfoService.createNewCategory(categoryDataRequest);
    }
}
