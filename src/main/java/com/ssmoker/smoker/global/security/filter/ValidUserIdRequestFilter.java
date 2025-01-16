package com.ssmoker.smoker.global.security.filter;

import com.ssmoker.smoker.domain.DeactivateUsers.repository.DeactivatedUsersRepository;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import com.ssmoker.smoker.global.security.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidUserIdRequestFilter {

    private final DeactivatedUsersRepository deactivatedUsersRepository;

    public void isDeletedUsers (long memberid){
        boolean existInDeactivatedUsersTable = deactivatedUsersRepository.existsById(memberid);
        if (existInDeactivatedUsersTable) {
            throw new AuthException(ErrorStatus.AUTH_INVALID_TOKEN);
        }
    }

}
