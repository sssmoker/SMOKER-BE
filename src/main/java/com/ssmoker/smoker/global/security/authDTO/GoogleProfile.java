package com.ssmoker.smoker.global.security.authDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleProfile {
    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String nickName;
}
