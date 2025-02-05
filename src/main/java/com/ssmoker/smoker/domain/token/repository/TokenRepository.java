package com.ssmoker.smoker.domain.token.repository;

import com.ssmoker.smoker.domain.token.domain.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByRefreshToken(String refreshToken);

    Optional<Token> findByMemberId(Long memberId);
}
