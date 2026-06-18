package com.garima.blogapp.controller;

import com.garima.blogapp.entity.Blog;
import com.garima.blogapp.repository.BlogRepository;
import com.garima.blogapp.service.S3Service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    private final BlogRepository blogRepository;
    private final S3Service s3Service;

    public BlogController(BlogRepository blogRepository,
                          S3Service s3Service) {
        this.blogRepository = blogRepository;
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public String uploadBlog(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam("image") MultipartFile image
    ) throws Exception {

        Blog blog = new Blog();

        blog.setTitle(title);
        blog.setContent(content);

        if (!image.isEmpty()) {
            String imageUrl = s3Service.uploadFile(image);
            blog.setImageUrl(imageUrl);
        }

        blogRepository.save(blog);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {

        blogRepository.deleteById(id);

        return "redirect:/";
    }
}