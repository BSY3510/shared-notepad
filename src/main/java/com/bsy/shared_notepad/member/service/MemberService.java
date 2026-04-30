package com.bsy.shared_notepad.member.service;

import com.bsy.shared_notepad.config.JwtProvider;
import com.bsy.shared_notepad.member.domain.Member;
import com.bsy.shared_notepad.member.domain.MemberRole;
import com.bsy.shared_notepad.member.domain.MemberToken;
import com.bsy.shared_notepad.member.dto.LoginResponse;
import com.bsy.shared_notepad.member.repository.MemberRepository;
import com.bsy.shared_notepad.member.repository.MemberTokenRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public Member signup(String email, String password, String name, MemberRole role) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이메일 중복");
        }
        String encryptedPassword = passwordEncoder.encode(password);
        Member member = Member.create(email, name, encryptedPassword, role);
        memberRepository.save(member);
        return member;
    }

    public LoginResponse login(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다"));
        if (!passwordEncoder.matches(password, member.getEncryptedPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다");
        }

        String accessToken = jwtProvider.createAccessToken(email);
        String refreshToken = jwtProvider.createRefreshToken(email);
        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(jwtProvider.getRefreshTokenExpiration() / 1000);

        Optional<MemberToken> optionalMemberToken = memberTokenRepository.findByMember(member);
        if (optionalMemberToken.isPresent()) {
            MemberToken existingToken = optionalMemberToken.get();
            existingToken.updateRefreshToken(refreshToken, localDateTime);
        } else {
            MemberToken memberToken = MemberToken.create(member, refreshToken, localDateTime);
            memberTokenRepository.save(memberToken);
        }

        return new LoginResponse(accessToken, refreshToken);
    }

    public void logout(String refreshToken) {
        MemberToken memberToken = memberTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new IllegalArgumentException("오류 발생"));

        memberTokenRepository.delete(memberToken);
    }

}
