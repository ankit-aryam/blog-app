package com.example.blog.controller;

import com.example.blog.dto.BlogRequest;
import com.example.blog.dto.BlogResponse;
import com.example.blog.services.BlogService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blogs")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<BlogResponse> createBlog(@RequestBody BlogRequest request){
        BlogResponse response = blogService.createBlog(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BlogResponse>> getAllBlogs(){
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<List<BlogResponse>> getMyBlogs(){
        return ResponseEntity.ok(blogService.getMyBlogs());
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<BlogResponse> updateBlog(@PathVariable Long id, @RequestBody BlogRequest request){
        return ResponseEntity.ok(blogService.updateBlog(id, request));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id){
        blogService.deleteBlog(id);
        return ResponseEntity.ok(Map.of("message", "Blog Deleted Successfully"));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<BlogResponse>> getAllBlogsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        return ResponseEntity.ok(blogService.getAllBlogsPaged(page, size, sortBy));
    }
}
