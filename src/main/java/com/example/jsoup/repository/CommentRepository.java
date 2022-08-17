package com.example.jsoup.repository;

import com.example.jsoup.json.Comment;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "comments", path = "comments")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    List<Comment> findByComment(@Param("comment") String comment);
}