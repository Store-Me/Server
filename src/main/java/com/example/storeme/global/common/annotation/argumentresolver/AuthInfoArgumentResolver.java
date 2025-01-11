package com.example.storeme.global.common.annotation.argumentresolver;

import com.example.storeme.global.common.annotation.AuthInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 인증에 성공한 유저의 id값을 반환해주는 Argument Resolver 클래스
 */
public class AuthInfoArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthInfo.class) !=null
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)  {
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        String userId = (String) req.getAttribute("userId");

        if (userId != null) {
            try {
                return Long.valueOf(userId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("userId must be a valid number");
            }
        }

        return null;
    }
}
