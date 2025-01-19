package com.ssmoker.smoker.domain.member.controller;

import com.ssmoker.smoker.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/member")
public class MemberController {
    private final MemberService memberService;
}