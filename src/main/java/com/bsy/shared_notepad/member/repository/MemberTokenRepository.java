package com.bsy.shared_notepad.member.repository;

import com.bsy.shared_notepad.member.domain.Member;
import com.bsy.shared_notepad.member.domain.MemberToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {

    Optional<MemberToken> findByRefreshToken(String refreshToken);
    Optional<MemberToken> findByMember(Member member);

}
