package com.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oa.common.Result;
import com.oa.entity.Post;
import com.oa.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/list")
    public Result<List<Post>> list(Post post) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        if (post != null) {
            if (StringUtils.hasText(post.getPostName())) {
                wrapper.like(Post::getPostName, post.getPostName());
            }
            if (StringUtils.hasText(post.getStatus())) {
                wrapper.eq(Post::getStatus, post.getStatus());
            }
        }
        wrapper.eq(Post::getDeleted, "0");
        wrapper.orderByAsc(Post::getPostSort);
        List<Post> list = postService.list(wrapper);
        return Result.success(list);
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('system:post:list')")
    public Result<List<Post>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            Post post) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        if (post != null) {
            if (StringUtils.hasText(post.getPostName())) {
                wrapper.like(Post::getPostName, post.getPostName());
            }
            if (StringUtils.hasText(post.getStatus())) {
                wrapper.eq(Post::getStatus, post.getStatus());
            }
        }
        wrapper.eq(Post::getDeleted, "0");
        wrapper.orderByAsc(Post::getPostSort);
        List<Post> list = postService.list(wrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:post:query')")
    public Result<Post> getById(@PathVariable Long id) {
        Post post = postService.getById(id);
        return Result.success(post);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:post:add')")
    public Result<Boolean> add(@RequestBody Post post) {
        post.setDeleted("0");
        boolean result = postService.save(post);
        return Result.success(result);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('system:post:edit')")
    public Result<Boolean> update(@RequestBody Post post) {
        boolean result = postService.updateById(post);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:post:remove')")
    public Result<Boolean> delete(@PathVariable Long id) {
        Post post = new Post();
        post.setId(id);
        post.setDeleted("1");
        boolean result = postService.updateById(post);
        return Result.success(result);
    }
}