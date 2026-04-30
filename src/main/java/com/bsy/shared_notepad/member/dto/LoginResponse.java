package com.bsy.shared_notepad.member.dto;

public record LoginResponse(
    String accessToken,
    String refreshToken
) {

}
