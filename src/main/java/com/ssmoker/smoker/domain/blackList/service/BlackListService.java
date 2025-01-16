package com.ssmoker.smoker.domain.blackList.service;

public interface BlackListService {
    void addToBlackList(long memberId,String accessToken);
}
