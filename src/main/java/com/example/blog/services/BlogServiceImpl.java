package com.example.blog.services;

import com.example.blog.dto.BlogRequest;
import com.example.blog.dto.BlogResponse;
import com.example.blog.entity.Blog;
import com.example.blog.entity.UserEntity;
import com.example.blog.repository.BlogRepository;
import com.example.blog.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public List<BlogResponse> getAllBlogs(){
        return blogRepository.findAll().stream().map(blog -> BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .createdAt(blog.getCreatedAt().toString())
                .authorName(blog.getUser().getName())
                .build()
        ).toList();
    }

    @Override
    public List<BlogResponse> getMyBlogs(){
        UserEntity currentUser = userUtils.getCurrentUser();

        return blogRepository.findByUser(currentUser).stream().map(blog -> BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .createdAt(blog.getCreatedAt().toString())
                .authorName(currentUser.getName())
                .build()
        ).toList();
    }

    @Override
    public BlogResponse updateBlog(Long blogId, BlogRequest request){
        UserEntity currentUser = userUtils.getCurrentUser();

        Blog blog = blogRepository.findById(blogId).orElseThrow(()-> new RuntimeException("Blog Not Found"));

        if(!blog.getUser().getId().equals(currentUser.getId())){
            throw new AccessDeniedException("You are not allowed to update this blog");
        }

        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());

        Blog updated = blogRepository.save(blog);

        return BlogResponse.builder()
                .id(updated.getId())
                .title(updated.getTitle())
                .content(updated.getContent())
                .createdAt(updated.getCreatedAt().toString())
                .authorName(currentUser.getName())
                .build();

    }

    @Override
    public void deleteBlog(Long blogId){
        UserEntity currentUser = userUtils.getCurrentUser();
        Blog blog = blogRepository.findById(blogId).orElseThrow(()-> new RuntimeException("Blog Not Found"));

        boolean isOwner = blog.getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You are not allowed to delete this blog");
        }

        blogRepository.delete(blog);
    }

    public Page<BlogResponse> getAllBlogsPaged(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        return blogRepository.findAll(pageable).map(blog -> BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .authorName(blog.getUser().getName())
                .createdAt(blog.getCreatedAt().toString())
                .build());
    }
}

