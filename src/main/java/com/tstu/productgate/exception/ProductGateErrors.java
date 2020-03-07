package com.tstu.productgate.exception;

import com.tstu.commons.exception.PrsErrorCode;

import java.util.Arrays;
import java.util.Optional;

/**
 * Перечисление всех возможных ошибок в product-gate-service
 */
public enum ProductGateErrors implements PrsErrorCode {

    EXPIRED_OR_INVALID_JWT_TOKEN(1, ProductGateExceptionMessage.EXPIRED_OR_INVALID_JWT_TOKEN_MSG),
    JSON_NOT_READABLE(3, ProductGateExceptionMessage.JSON_NOT_READABLE),
    ACCESS_DENIED(4, ProductGateExceptionMessage.ACCESS_DENIED_MSG),
    UNSUPPORTED_REVIEW_SYSTEM(6, ProductGateExceptionMessage.UNSUPPORTED_REVIEW_SYSTEM_MSG),
    AUTHENTICATION_REQUEST_ERROR(7, ProductGateExceptionMessage.AUTHENTICATION_REQUEST_ERROR_MSG),
    PRODUCT_INFO_REQUEST_ERROR(8, ProductGateExceptionMessage.PRODUCT_INFO_REQUEST_ERROR_MSG),
    PRODUCT_DETERMINATION_REQUEST_ERROR(9, ProductGateExceptionMessage.PRODUCT_DETERMINATION_REQUEST_ERROR_MSG),
    UNEXPECTED_ERROR(10, ProductGateExceptionMessage.UNEXPECTED_ERROR_MSG),
    CONNECTION_ERROR(11, ProductGateExceptionMessage.CONNECTION_ERROR_MSG);

    private Integer errorCode;
    private String errorDescription;

    ProductGateErrors(Integer errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public static Optional<ProductGateErrors> getByDescription(String errorDescription) {
        return Arrays.stream(values())
                .filter(productGateErrors ->  productGateErrors.errorDescription.equals(errorDescription))
                .findFirst();
    }
}
