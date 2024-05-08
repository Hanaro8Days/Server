package com.hana.app.controller;

import com.github.pagehelper.PageInfo;
import com.hana.app.data.dto.CommentDto;
import com.hana.app.data.dto.PostDto;
import com.hana.app.service.CommentService;
import com.hana.app.data.dto.UserDto;
import com.hana.app.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    final PostService postService;
    final CommentService commentService;

    String dir = "posts/";

    @GetMapping("")
    public String main(Model model, @RequestParam("id") Integer postId, HttpSession httpSession) throws Exception {
        Integer userId = (Integer) httpSession.getAttribute("id");

        PostDto postDto = postService.getPostInfo(postId, userId);
        List<CommentDto> commentDtoList = commentService.getIsLikedComment(postId, userId);

        model.addAttribute("id", httpSession.getAttribute("id"));
        model.addAttribute("postId", postId);
        model.addAttribute("post", postDto);
        model.addAttribute("boardId", postDto.getBoardId());
        model.addAttribute("comments", commentDtoList);
        model.addAttribute("center", dir + "post");


        return "index";
    }

    @PostMapping("/writecomment")
    public String writeComment(PostDto postDto, CommentDto commentDto, HttpSession httpSession) throws Exception {
        Object id = httpSession.getAttribute("id");
        commentDto.setUserDto((UserDto.builder().userId(Integer.parseInt(String.valueOf(id))).build()));
        commentDto.setPostId(postDto.getPostId());

        log.info(commentDto.toString());

        if(commentDto.isAnonymous()) { // 익명 체크했으면
            commentService.addByAnonymous(commentDto);
        } else { // 익명 체크 안했으면
            commentService.addByNotAnonymous(commentDto);
        }


        //model.addAttribute("center", dir + "writecomment");


        return "redirect:/posts/?id=" + postDto.getPostId();
    }

    @GetMapping("/writepost")
    public String writePost(Model model, @RequestParam("boardId") Integer boardId) {
        model.addAttribute("boardId", boardId);
        model.addAttribute("center", dir + "writepost");

        return "index";
    }

    @PostMapping("/writepost")
    public String writePost(Model model, PostDto postDto, HttpSession httpSession) throws Exception {
        Object id = httpSession.getAttribute("id");
        postDto.setUserDto((UserDto.builder().userId(Integer.parseInt(String.valueOf(id))).build()));

        if(postDto.isAnonymous()) { // 익명 체크했으면
            postService.addByAnonymous(postDto);
        } else { // 익명 체크 안했으면
            postService.addByNotAnonymous(postDto);
        }

        model.addAttribute("center", dir + "writepost");

        return "redirect:/boards?id=" + postDto.getBoardId();
    }

    @GetMapping("/deletePost")
    public String deletePost(@RequestParam("postId") int postId, @RequestParam("boardId") int boardId) throws Exception {
        postService.del(postId);

        return "redirect:/boards?id=" + boardId;
    }
    @GetMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentId") int commentId, @RequestParam("postId") int postId) throws Exception {
        commentService.del(commentId);

        return "redirect:/posts?id=" + postId;
    }
}
