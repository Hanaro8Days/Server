package com.hana.app.controller;

import com.hana.app.data.dto.BoardDto;
import com.hana.app.data.dto.CommentDto;
import com.hana.app.data.dto.PostDto;
import com.hana.app.service.BoardService;
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

    String dir = "posts/";

    final PostService postService;
    final CommentService commentService;
    final BoardService boardService;

    @GetMapping("")
    public String main(Model model, @RequestParam("id") Integer postId, HttpSession httpSession) throws Exception {
        Integer userId = (Integer) httpSession.getAttribute("id");
        PostDto postDto = postService.getPostInfo(postId, userId);
        List<CommentDto> commentDtoList = commentService.getIsLikedComment(postId, userId);
        BoardDto boardDto = boardService.get(postDto.getBoardId());
        String boardName = boardDto.getName();

        model.addAttribute("id", httpSession.getAttribute("id"));
        model.addAttribute("postId", postId);
        model.addAttribute("post", postDto);
        model.addAttribute("boardId", postDto.getBoardId());
        model.addAttribute("comments", commentDtoList);
        model.addAttribute("boardName", boardName);
        model.addAttribute("center", dir + "post");

        return "index";
    }


    @ResponseBody
    @PostMapping("/writecomment")
    public String writeComment(@RequestParam("postId") Integer postId, CommentDto commentDto, HttpSession httpSession) throws Exception {
        Integer id = (Integer) httpSession.getAttribute("id");
        commentDto.setUserDto((UserDto.builder().userId(id).build()));
        commentDto.setPostId(postId);

        if (commentDto.isAnonymous()) { // 익명 체크했으면
            commentService.addByAnonymous(commentDto);
        } else { // 익명 체크 안했으면
            commentService.addByNotAnonymous(commentDto);
        }

        return "1";
    }

    @GetMapping("/writepost")
    public String writePost(Model model, @RequestParam(value = "boardId") Integer boardId, @RequestParam("pageNo") Integer pageNo) {
        model.addAttribute("boardId", boardId);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("center", dir + "writepost");

        return "index";
    }

    @ResponseBody
    @PostMapping("/writepost")
    public String writePost(Model model, PostDto postDto, HttpSession httpSession) throws Exception {
        Integer id = (Integer) httpSession.getAttribute("id");
        postDto.setUserDto((UserDto.builder().userId(id).build()));

        if (postDto.isAnonymous()) { // 익명 체크했으면
            postService.addByAnonymous(postDto);
        } else { // 익명 체크 안했으면
            postService.addByNotAnonymous(postDto);
        }

        return "1";
    }

    @GetMapping("/updatepost")
    public String updatePost(Model model, @RequestParam("postId") Integer postId, HttpSession httpSession) throws Exception {
        Integer userId = (Integer) httpSession.getAttribute("id");
        PostDto postDto = postService.getPostInfo(postId, userId);

        model.addAttribute("postId", postId);
        model.addAttribute("post", postDto);
        model.addAttribute("center", dir + "updatepost");

        return "index";
    }

    @ResponseBody
    @PostMapping("/updatepost")
    public String updatePost(PostDto postDto) throws Exception {
        postService.modify(postDto);

        return "1";
    }

    @ResponseBody
    @DeleteMapping("/deletePost")
    public String deletePost(@RequestParam("postId") int postId) throws Exception {
        postService.del(postId);

        return "1";
    }

    @ResponseBody
    @DeleteMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentId") int commentId) throws Exception {
        commentService.del(commentId);

        return "1";
    }

}
