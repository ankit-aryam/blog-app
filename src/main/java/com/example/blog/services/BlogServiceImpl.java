package com.example.blog.services;

import com.example.blog.dto.BlogRequest;
import com.example.blog.dto.BlogResponse;
import com.example.blog.entity.Blog;
import com.example.blog.entity.UserEntity;
import com.example.blog.repository.BlogRepository;
import com.example.blog.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserUtils userUtils;

    @Override
    public BlogResponse createBlog(BlogRequest request) {
        UserEntity currentUser = userUtils.getCurrentUser();

        Blog blog = Blog.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .user(currentUser)
                .build();

        Blog saved = blogRepository.save(blog);

        return BlogResponse.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .content(saved.getContent())
                .createdAt(saved.getCreatedAt().toString())
                .authorName(currentUser.getName())
                .build();
    }
}

