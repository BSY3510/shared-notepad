package com.bsy.shared_notepad.folder.domain;

import com.bsy.shared_notepad.member.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "folder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Folder parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Folder> children = new ArrayList<>();

    private String name;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    protected void addChild(Folder child) {
        children.add(child);
        child.parent = this;
    }

    private Folder(Member member, String name, Folder parent) {
        this.member = member;
        this.name = name;
        this.parent = parent;
    }

    public static Folder createRoot(Member member, String name) {
        return new Folder(member, name, null);
    }

    public static Folder createChild(Member member, String name, Folder parent) {
        if (!parent.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("부모 폴더와 회원이 다름");
        }

        Folder folder = new Folder(member, name, parent);
        parent.addChild(folder);

        return folder;
    }

}
