package com.ssmoker.smoker.global.security.handler.resolver;

import com.ssmoker.smoker.global.exception.SmokerClientException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import com.ssmoker.smoker.global.security.handler.annotation.AuthUser;
import com.ssmoker.smoker.global.security.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUser.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory)
            throws SmokerClientException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = null;
        if (authentication != null) {

            if (authentication.getName().equals("anonymousUser")) {
                throw new SmokerClientException(ErrorStatus._UNAUTHORIZED);
            }
            principal = authentication.getPrincipal();
        }
        if (principal == null || principal.getClass() == String.class) {
            throw new SmokerClientException(ErrorStatus.USER_NOT_FOUND);
        }

        if (principal instanceof PrincipalDetails) {
            PrincipalDetails userDetails = (PrincipalDetails) principal;
            return userDetails.getId();
        }
        throw new SmokerClientException(ErrorStatus.USER_NOT_FOUND);
    }
}
