package com.example.blog.services;

import com.example.blog.dto.BlogRequest;
import com.example.blog.dto.BlogResponse;

public interface BlogService {
    BlogResponse createBlog(BlogRequest request);
}
