package com.ssmoker.smoker.test;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class TestRequestDto {
    private String content;
    private String name;
    private MultipartFile multipartFile;

    public Test of(String url) {
        return new Test(this.name, this.content, url);
    }
}
