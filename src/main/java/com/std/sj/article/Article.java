package com.std.sj.article;

import com.std.sj.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 25)
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDateTime createDate;
    @ManyToOne
    private SiteUser author;
    private LocalDateTime modifyDate;
}
