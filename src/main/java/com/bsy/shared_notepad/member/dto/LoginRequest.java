package com.bsy.shared_notepad.member.dto;

public record LoginRequest(
    String email,
    String password
) {

}
