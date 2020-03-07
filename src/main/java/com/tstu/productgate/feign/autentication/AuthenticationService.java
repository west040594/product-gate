package com.tstu.productgate.feign.autentication;

import com.tstu.commons.dto.http.request.authenticationsystem.AuthenticationRequest;
import com.tstu.commons.dto.http.request.authenticationsystem.UserDataRequest;
import com.tstu.commons.dto.http.response.authenticationsystem.AuthenticationResponse;
import com.tstu.commons.dto.http.response.authenticationsystem.UserResponse;
import com.tstu.productgate.config.FeignProperties;
import com.tstu.productgate.components.RequestContext;
import com.tstu.productgate.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final RequestContext requestContext;
    private final AuthenticationClient authenticationClient;
    private final FeignProperties feignProperties;
    private final ModelMapper modelMapper;

    /**
     * Получение пользователя из authentication-service по токену
     * @return Полученный пользователь
     */
    public User getUserByToken() {
        log.info("Проверка валидности токена в authentication service");
        requestContext.setRequestService(feignProperties.getServices().getAuthentication().getName());
        UserResponse userResponse = authenticationClient.getUserByToken(requestContext.getToken()).getBody();
        User user = modelMapper.map(userResponse, User.class);
        user.setJwtToken(requestContext.getToken());
        log.info("Пользователь по токену опознан - {}", user.getUsername());
        return user;
    }

    /**
     * Авторизация пользователя в authentication-service
     * @param authRequest Структура запроса на авторизацию
     * @return Ответ авторизации
     */
    public AuthenticationResponse signIn(AuthenticationRequest authRequest) {
        log.info("Авторизация в  authentication service. Запрос - {}", authRequest);
        requestContext.setRequestService(feignProperties.getServices().getAuthentication().getName());
        AuthenticationResponse authenticationResponse = authenticationClient.login(authRequest).getBody();
        requestContext.setToken("Bearer " + authenticationResponse.getToken());
        log.info("Пользователь авторизован - {}", authenticationResponse.getUser().getUsername());
        return authenticationResponse;
    }

    public AuthenticationResponse singUp(UserDataRequest userDataRequest) {
        log.info("Регистрация в  authentication service. Запрос - {}", userDataRequest);
        requestContext.setRequestService(feignProperties.getServices().getAuthentication().getName());
        AuthenticationResponse authenticationResponse = authenticationClient.sigup(userDataRequest).getBody();
        log.info("Пользователь зарегестрирован и авторизован - {}", authenticationResponse.getUser().getUsername());
        return authenticationResponse;
    }
}
