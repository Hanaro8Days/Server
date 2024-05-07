package com.hana.app.controller;

import com.hana.app.data.dto.PostDto;
import com.hana.app.data.dto.UserDto;
import com.hana.app.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    final PostService postService;

    String dir = "posts/";

    @GetMapping("")
    public String main(Model model, @RequestParam("id") Integer postId, HttpSession httpSession) {
        model.addAttribute("id", httpSession.getAttribute("id"));
        model.addAttribute("center", dir + "post");

        return "index";
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
}
