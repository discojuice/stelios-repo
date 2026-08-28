package com.example.demo.repository;

import java.util.List;
import com.example.demo.entity.BlogPost;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

        List<BlogPost> findAllByOrderByCreatedOnDesc();

        List<BlogPost> findByGroupIdInOrderByCreatedOnDesc(List<Integer> groupIds);

        @Query("SELECT p.groupId FROM BlogPost p GROUP BY p.groupId ORDER BY MAX(p.createdOn) DESC")
        List<Integer> findDistinctGroupIds(Pageable pageable);

}
