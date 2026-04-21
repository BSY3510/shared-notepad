package com.bsy.shared_notepad.member;

import com.bsy.shared_notepad.config.JpaAuditingConfig;
import com.bsy.shared_notepad.member.domain.Member;
import com.bsy.shared_notepad.member.domain.MemberRole;
import com.bsy.shared_notepad.member.repository.MemberRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaAuditingConfig.class)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Member 생성 및 DB에 저장이 잘 되는지 확인")
    void saveMember() {
        Member member = Member.create("test@test.com", "David", "password", MemberRole.USER);

        Member savedMember = memberRepository.save(member);

        Assertions.assertThat(member.getEmail()).isEqualTo(savedMember.getEmail());
        Assertions.assertThat(member.getId()).isNotNull();
        Assertions.assertThat(member.getCreatedAt()).isNotNull();
        Assertions.assertThat(member.getUpdatedAt()).isNotNull();

        Assertions.assertThat(member).isSameAs(savedMember);
        Assertions.assertThat(memberRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("DB에 Member 조회 잘 되는지 확인")
    void searchMember() {
        memberRepository.save(Member.create("test01@test.com", "Test001", "password", MemberRole.USER));
        memberRepository.save(Member.create("test02@test.com", "Test002", "password", MemberRole.USER));
        memberRepository.save(Member.create("test03@test.com", "Test003", "password", MemberRole.USER));
        memberRepository.save(Member.create("test04@test.com", "Test004", "password", MemberRole.USER));
        memberRepository.save(Member.create("test05@test.com", "Test005", "password", MemberRole.USER));

        Assertions.assertThat(memberRepository.count()).isEqualTo(5);

        Optional<Member> result01 = memberRepository.findByEmail("test03@test.com");
        Assertions.assertThat(result01).isPresent();
        Assertions.assertThat(result01.get().getName()).isEqualTo("Test003");

        Optional<Member> result02 = memberRepository.findByEmail("test12@test.com");
        Assertions.assertThat(result02).isEmpty();
    }

    @Test
    @DisplayName("DB에 Member 유무 확인")
    void checkMember() {
        memberRepository.save(Member.create("test01@test.com", "Test001", "password", MemberRole.USER));
        memberRepository.save(Member.create("test02@test.com", "Test002", "password", MemberRole.USER));
        memberRepository.save(Member.create("test03@test.com", "Test003", "password", MemberRole.USER));
        memberRepository.save(Member.create("test04@test.com", "Test004", "password", MemberRole.USER));
        memberRepository.save(Member.create("test05@test.com", "Test005", "password", MemberRole.USER));

        Assertions.assertThat(memberRepository.count()).isEqualTo(5);

        boolean result01 = memberRepository.existsByEmail("test03@test.com");
        Assertions.assertThat(result01).isTrue();

        boolean result02 = memberRepository.existsByEmail("test12@test.com");
        Assertions.assertThat(result02).isFalse();
    }

}
