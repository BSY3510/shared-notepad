package com.bsy.shared_notepad.member.dto;

import com.bsy.shared_notepad.member.domain.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
    @Email(message = "올바른 형식이 아닙니다.")
    @NotBlank
    String email,
    String password,
    String name,
    MemberRole role
) {

}
