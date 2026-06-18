package com.garima.blogapp.controller;

import com.garima.blogapp.entity.Blog;
import com.garima.blogapp.repository.BlogRepository;
import com.garima.blogapp.service.S3Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {

    private final BlogRepository blogRepository;
    private final S3Service s3Service;

    public HomeController(
            BlogRepository blogRepository,
            S3Service s3Service
    ) {
        this.blogRepository = blogRepository;
        this.s3Service = s3Service;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute(
                "blogs",
                blogRepository.findAll()
        );

        return "index";
    }

    @PostMapping("/create-blog")
    public String createBlog(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam("image") MultipartFile image
    ) throws Exception {

        Blog blog = new Blog();

        blog.setTitle(title);
        blog.setContent(content);

        if (!image.isEmpty()) {

            String imageUrl =
                    s3Service.uploadFile(image);

            blog.setImageUrl(imageUrl);
        }

        blogRepository.save(blog);

        return "redirect:/";
    }

    @PostMapping("/delete-blog/{id}")
    public String deleteBlog(@PathVariable Long id) {

        blogRepository.deleteById(id);

        return "redirect:/";
    }

    @GetMapping("/edit-blog/{id}")
    public String editBlog(
            @PathVariable Long id,
            Model model
    ) {

        Blog blog = blogRepository
                .findById(id)
                .orElseThrow();

        model.addAttribute(
                "blog",
                blog
        );

        return "edit";
    }

    @PostMapping("/update-blog/{id}")
    public String updateBlog(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content
    ) {

        Blog blog = blogRepository
                .findById(id)
                .orElseThrow();

        blog.setTitle(title);
        blog.setContent(content);

        blogRepository.save(blog);

        return "redirect:/";
    }
}