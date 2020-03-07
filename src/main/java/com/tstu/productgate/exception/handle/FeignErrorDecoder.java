package com.tstu.productgate.exception.handle;

import com.tstu.commons.exception.PrsHttpException;
import com.tstu.productgate.config.FeignProperties;
import com.tstu.productgate.exception.ProductGateErrors;
import com.tstu.productgate.util.FeignUtil;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {

    public final FeignProperties feignProperties;

    /**
     * Обработка исключения в случае если возникла ошибка при вызове сервисов(info, authentication, determination)
     * Алгоритм:
     * 1. Получение тела ответа ошибки от вызываемого сервиса
     * 2. Получение наименования вызываемого сервиса из http заголовка Service
     * 3. Переопределение нового исключения в зависимости от наименования вызываемого сервиса для конкретизации где именно произошла ошибка
     * @param methodKey
     * @param response  Тело ответа из вызываемого сервиса
     * @return Новое сгенирирование исключение
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        String responseBody = FeignUtil.getResponseBody(response);
        String requestService = "";
        if(Objects.nonNull(response.request().headers().get("Service"))) {
            requestService = response.request().headers().get("Service").stream().findFirst().get();
        }
        log.info("Ошибка - {}", responseBody);
        if(requestService.equals(feignProperties.getServices().getProductinfo().getName())) {
            return new PrsHttpException(ProductGateErrors.PRODUCT_INFO_REQUEST_ERROR, responseBody, HttpStatus.UNPROCESSABLE_ENTITY);
        } else if(requestService.equals(feignProperties.getServices().getAuthentication().getName())) {
            return new PrsHttpException(ProductGateErrors.AUTHENTICATION_REQUEST_ERROR, responseBody, HttpStatus.FORBIDDEN);
        } else if(requestService.equals(feignProperties.getServices().getProductdetermination().getName())) {
            return new PrsHttpException(ProductGateErrors.PRODUCT_DETERMINATION_REQUEST_ERROR, responseBody, HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            return new PrsHttpException(ProductGateErrors.UNEXPECTED_ERROR, responseBody,  HttpStatus.BAD_REQUEST);
        }
    }
}
