package com.tstu.productgate.exception;

import com.tstu.commons.exception.ExceptionMessage;

public interface ProductGateExceptionMessage extends ExceptionMessage {
    String AUTHENTICATION_REQUEST_ERROR_MSG = "Не верный запрос в Authentication Service";
    String PRODUCT_INFO_REQUEST_ERROR_MSG = "Не верный запрос в Product-Info Service";
    String PRODUCT_DETERMINATION_REQUEST_ERROR_MSG = "Не верный запрос в Product-Determination Service";
    String CONNECTION_ERROR_MSG = "Не удалось подключится к сервису";
    String COULD_NOT_DETERMINE_PRODUCT_ERROR_MSG = "Не удалось определить наименование продукта по изображению";
}
