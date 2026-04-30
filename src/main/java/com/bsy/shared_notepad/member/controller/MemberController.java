package com.bsy.shared_notepad.member.controller;

import com.bsy.shared_notepad.member.domain.Member;
import com.bsy.shared_notepad.member.domain.MemberRole;
import com.bsy.shared_notepad.member.dto.LoginRequest;
import com.bsy.shared_notepad.member.dto.LoginResponse;
import com.bsy.shared_notepad.member.dto.LogoutRequest;
import com.bsy.shared_notepad.member.dto.SignupRequest;
import com.bsy.shared_notepad.member.dto.SignupResponse;
import com.bsy.shared_notepad.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public SignupResponse signup(
        @Valid @RequestBody SignupRequest signupRequest
    ) {
        Member member = memberService.signup(signupRequest.email(), signupRequest.password(), signupRequest.name(), signupRequest.role());

        return new SignupResponse(member.getId(), member.getEmail());
    }

    @PostMapping("/login")
    public LoginResponse login(
        @RequestBody LoginRequest loginRequest
    ) {
        return memberService.login(loginRequest.email(), loginRequest.password());
    }

    @PostMapping("/logout")
    public void logout(
        @RequestBody LogoutRequest logoutRequest
    ) {
        memberService.logout(logoutRequest.refreshToken());
    }
}
