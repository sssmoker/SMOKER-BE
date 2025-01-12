package com.ssmoker.smoker.security.handler.resolver;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.service.MemberService;
import com.ssmoker.smoker.global.exception.AuthException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import com.ssmoker.smoker.security.principal.PrincipalDetails;
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
    private final MemberService memberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory)
            throws AuthException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = null;
        if (authentication != null) {
            // 로그인하지 않은 익명 사용자라면 null 반환
            if (authentication.getName().equals("anonymousUser")) {
                return null;
            }
            principal = authentication.getPrincipal();
        }
        if (principal == null || principal.getClass() == String.class) {
            throw new AuthException(ErrorStatus.USER_NOT_FOUND);
        }

        if (principal instanceof PrincipalDetails) {
            PrincipalDetails userDetails = (PrincipalDetails) principal;
            Long userId = userDetails.getId();
            return memberService.findMemberById(userId);
        }
        return null;
    }
}
