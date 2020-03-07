package com.tstu.productgate.util;

import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;

@Slf4j
public class FeignUtil {
    public static String getResponseBody(Response response) {
        try {
            String s = feign.Util.toString(response.body().asReader());
            return s;
        } catch (IOException e) {
            return Strings.EMPTY;
        }
    }
}
