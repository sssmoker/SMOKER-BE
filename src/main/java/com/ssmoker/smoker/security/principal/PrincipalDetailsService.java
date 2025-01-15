package com.ssmoker.smoker.security.principal;

import com.ssmoker.smoker.domain.member.domain.Member;
import com.ssmoker.smoker.domain.member.repository.MemberRepository;
import com.ssmoker.smoker.security.exception.AuthException;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member =
                memberRepository
                        .findById(Long.parseLong(userId))
                        .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));

        return new PrincipalDetails(member);
    }
}
