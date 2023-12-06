package com.std.sj.article;

import com.std.sj.user.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private Specification<Article> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Article> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Article, SiteUser> u1 = q.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"));   // 질문 작성자

            }
        };
    }


    public Page<Article> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Article> spec = search(kw);
        return this.articleRepository.findAll(spec, pageable);
    }


    public Article getArticle(Integer id) {
        Optional<Article> article = this.articleRepository.findById(id);
        return article.get();
    }

    public Article create(String subject, String content, SiteUser user) {
        Article a = new Article();
        a.setContent(content);
        a.setSubject(subject);
        a.setAuthor(user);
        a.setCreateDate(LocalDateTime.now());
        this.articleRepository.save(a);
        return a;
    }

    public Article modify(Article article, String subject, String content) {
        article.setSubject(subject);
        article.setContent(content);
        article.setModifyDate(LocalDateTime.now());
        return this.articleRepository.save(article);
    }

    public void delete(Article article) {
        this.articleRepository.delete(article);
    }
}
