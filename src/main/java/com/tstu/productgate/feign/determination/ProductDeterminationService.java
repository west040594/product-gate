package com.tstu.productgate.feign.determination;

import com.tstu.commons.dto.http.response.determination.PredictionResponse;
import com.tstu.productgate.config.FeignProperties;
import com.tstu.productgate.components.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductDeterminationService {

    private final FeignProperties feignProperties;
    private final RequestContext requestContext;
    private final ProductDeterminationClient productDeterminationClient;

    /**
     * Предсказание продукта по изображение в product-determination-service
     * @param file Изображение продукта
     * @return Стуктура ответа предсказания. Содержит список наименований продуктов с коэфициентом совпадения наиболее подходящие под данное изображение
     */
    public PredictionResponse predictImage(MultipartFile file, String modelName) {
        log.info("Запрос на предсказания наименования продукта {} в product-determination", modelName);
        requestContext.setRequestService(feignProperties.getServices().getProductdetermination().getName());
        PredictionResponse predictionResponse = productDeterminationClient.predictImage(file, modelName).getBody();
        log.info("Наименования - {}", predictionResponse.getProducts());
        return predictionResponse;
    }

    /**
     * Получение всех возможных наименований продуктов в нейронной модели
     * @return Список наименований продуктов
     */
    public List<String> getClassLabels(String modelName) {
        log.info("Запрос на получение все наименоваий продуктов {} нейронной модели", modelName);
        requestContext.setRequestService(feignProperties.getServices().getProductdetermination().getName());
        List<String> labels = productDeterminationClient.getClassLabels(modelName).getBody();
        log.info("Наименования - {}", labels);
        return labels;
    }

    /**
     * Получение всех возможных псевдонимов категорий нейронных моделей
     * @return Список псевдонимов
     */
    public Set<String> getAliases() {
        log.info("Запрос на получение все псевдонимов категорий в product-determination");
        requestContext.setRequestService(feignProperties.getServices().getProductdetermination().getName());
        Set<String> aliases = productDeterminationClient.getAliases().getBody();
        log.info("Псевдонимы - {}", aliases);
        return aliases;
    }

    /**
     * Запрос на запуск повторного обучения нейронной модели
     * @return Стаутус запроса из product-determination
     */
    public String trainModel(String modelName) {
        log.info("Запрос на обучение нейронной модели - {}", modelName);
        requestContext.setRequestService(feignProperties.getServices().getProductdetermination().getName());
        String response = productDeterminationClient.trainModel(modelName).getBody();
        log.info("Ответ на создание модели - {}", response);
        return response;
    }
}
