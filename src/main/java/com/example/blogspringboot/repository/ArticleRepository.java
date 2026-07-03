package com.example.blogspringboot.repository;

import com.example.blogspringboot.entity.Article;
import com.example.blogspringboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByAuthor(User author);

    List<Article> findByAuthorId(Long authorId);


    @Query("SELECT a FROM Article a JOIN a.tags t WHERE t.name = :tagName")
    List<Article> findByTagName(@Param("tagName") String tagName);
}