package com.tstu.productgate.feign.productinfo;

import com.tstu.commons.dto.http.request.productinfo.CategoryDataRequest;
import com.tstu.commons.dto.http.request.productinfo.ProductDataRequest;
import com.tstu.commons.dto.http.response.productinfo.CategoryResponse;
import com.tstu.commons.dto.http.response.productinfo.ProductResponse;
import com.tstu.productgate.components.RequestContext;
import com.tstu.productgate.config.FeignProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductInfoService {

    private final ProductInfoClient productInfoClient;
    private final FeignProperties feignProperties;
    private final RequestContext requestContext;

    /**
     * Получение продуктов в product-info-service по их наименованиям
     * @param productNames Коллекция наименований продуктов
     * @return Коллекция продуктов
     */
    public List<ProductResponse> getAllProductsByPredict(List<String> productNames) {
        log.info("Запрос на получении списка продуктов в product-info - {}", productNames);
        requestContext.setRequestService(feignProperties.getServices().getProductinfo().getName());
        List<ProductResponse> productResponses = productInfoClient.getAllProductsByPredict(productNames).getBody();
        log.info("Продукты - {}", productResponses.stream().map(ProductResponse::getName).collect(Collectors.toList()));
        return productResponses;
    }

    /**
     * Создание нового продукта в product-info-service
     * @param productDataRequest Структура запроса на добавление нового продукта
     * @return Созданный продукт
     */
    public ProductResponse createNewProduct(ProductDataRequest productDataRequest) {
        log.info("Запрос на добавление продукта в product-info - {}", productDataRequest);
        requestContext.setRequestService(feignProperties.getServices().getProductinfo().getName());
        ProductResponse productResponse = productInfoClient.createNewProduct(productDataRequest).getBody();
        log.info("Продукт - {}", productResponse.getName());
        return productResponse;
    }

    /**
     * Получение продукта по id в product-info-service
     * @param id Id продукта
     * @return Найденный продукт
     */
    public ProductResponse getProductById(Long id) {
        log.info("Поиск продукта по id в product-info. Id - {}", id);
        requestContext.setRequestService(feignProperties.getServices().getProductinfo().getName());
        ProductResponse productById = productInfoClient.getProductById(id).getBody();
        log.info("Продукт - {}", productById.getName());
        return productById;
    }

    /**
     * Получение всех продуктов в product-info-service
     * @return Продукты
     */
    public List<ProductResponse> getAllProducts() {
        log.info("Поиск всех продуктов в product-info");
        requestContext.setRequestService(feignProperties.getServices().getProductinfo().getName());
        return productInfoClient.getAllProducts().getBody();
    }

    /**
     * Получение продуктов по id в product-info-service
     * @param categoryName категория продукта
     * @return Найденный продукт
     */
    public List<ProductResponse> getProductsByCategoryName(String categoryName) {
        log.info("Поиск всех продуктов по категории - {} в product-info", categoryName);
        requestContext.setRequestService(feignProperties.getServices().getProductinfo().getName());
        return productInfoClient.getProductsByCategoryName(categoryName).getBody();
    }

    /**
     * Запрос на получение отзывов от продукте в review-dom-parser
     * @param id Id продукта
     * @return Информация о выполнениии запроса
     */
    public String sendDomParserRequest(Long id) {
        log.info("Запрос на парсинг отзывов о продукте по id в product-info. Id - {}", id);
        requestContext.setRequestService(feignProperties.getServices().getProductinfo().getName());
        return productInfoClient.sendDomParserRequest(id).getBody();
    }


    /**
     * Запрос на удаление продукта по id в product-info-service
     * @param id Id продукта
     * @return Информация о выполнениии запроса
     */
    public String deleteProductById(Long id) {
        log.info("Запрос на удаление продукта по id в product-info. Id - {}", id);
        requestContext.setRequestService(feignProperties.getServices().getProductinfo().getName());
        return productInfoClient.deleteProductById(id).getBody();
    }


    /**
     * Создание новой категории в product-info-service
     * @param categoryDataRequest Структура запроса на добавление новой категории
     * @return Созданная категория
     */
    public CategoryResponse createNewCategory(CategoryDataRequest categoryDataRequest) {
        log.info("Запрос на добавление категории в product-info - {}", categoryDataRequest);
        requestContext.setRequestService(feignProperties.getServices().getProductinfo().getName());
        CategoryResponse categoryResponse = productInfoClient.createNewCategory(categoryDataRequest).getBody();
        log.info("Категория - {}", categoryResponse.getName());
        return categoryResponse;
    }


    /**
     * Получение категории по id в product-info-service
     * @param id Id категории
     * @return Найденная категория
     */
    public CategoryResponse getCategoryById(Long id) {
        log.info("Поиск категории по id в product-info. Id - {}", id);
        requestContext.setRequestService(feignProperties.getServices().getProductinfo().getName());
        CategoryResponse categoryResponse = productInfoClient.getCategoryById(id).getBody();
        log.info("Категория - {}", categoryResponse);
        return categoryResponse;
    }

    /**
     * Получение категории по его наименованию в product-info-service
     * @param categoryName Наименование категории
     * @return Найденная категория
     */
    public CategoryResponse getCategoryByName(String categoryName) {
        log.info("Поиск категории по наименованию в product-info. name - {}", categoryName);
        requestContext.setRequestService(feignProperties.getServices().getProductinfo().getName());
        CategoryResponse categoryResponse = productInfoClient.getCategoryByName(categoryName).getBody();
        log.info("Категория - {}", categoryResponse);
        return categoryResponse;
    }

    /**
     * Получение всех категорий в product-info-service
     * @return Продукты
     */
    public List<CategoryResponse> getAllCategories() {
        log.info("Поиск всех категорий в product-info");
        requestContext.setRequestService(feignProperties.getServices().getProductinfo().getName());
        return productInfoClient.getAllCategories().getBody();
    }


    /**
     * Запрос на удаление категории по id в product-info-service
     * @param id Id категории
     * @return Информация о выполнениии запроса
     */
    public String deleteCategoryById(Long id) {
        log.info("Запрос на удаление категории по id в product-info. Id - {}", id);
        requestContext.setRequestService(feignProperties.getServices().getProductinfo().getName());
        return productInfoClient.deleteCategoryById(id).getBody();
    }

}
