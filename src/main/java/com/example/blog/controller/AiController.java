package com.example.blog.controller;

import com.example.blog.dto.AiRequest;
import com.example.blog.exception.InvalidInputException;
import com.example.blog.services.AiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@SecurityRequirement(name="bearerAuth")
public class AiController {

    private final AiService aiService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateBlog(@RequestBody AiRequest req){
        String prompt = req.getPrompt();

        if(prompt == null || prompt.isBlank()){
            throw new InvalidInputException("Prompt can not be empty");
        }

        String result = aiService.generateBlog(prompt);

        return ResponseEntity.ok(Map.of("content", result));
    }
}
