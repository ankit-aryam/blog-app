package com.example.blog.services;

import com.example.blog.dto.BlogRequest;
import com.example.blog.dto.BlogResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BlogService {
    BlogResponse createBlog(BlogRequest request);
    List<BlogResponse> getAllBlogs();
    List<BlogResponse> getMyBlogs();
    BlogResponse updateBlog(Long blogId, BlogRequest request);
    void deleteBlog(Long blogId);
    Page<BlogResponse> getAllBlogsPaged(int page, int size, String sortBy);
}
