package com.example.edu_board.controller;

import com.example.edu_board.dto.CreateUserDto;
import com.example.edu_board.dto.LoginUserDto;
import com.example.edu_board.entity.Board;
import com.example.edu_board.entity.User;
import com.example.edu_board.service.BoardService;
import com.example.edu_board.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final BoardService boardService;

    // 메인 화면
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // 유저 생성 폼
    @GetMapping("/createUserForm")
    public String createUserForm() {
        return "createUserForm";
    }

    // 유저 생성
    @PostMapping("/createUser")
    public String createUser(@ModelAttribute CreateUserDto userDto, RedirectAttributes rttr) {
        boolean isCreated = userService.createUser(userDto);

        if(isCreated) {
            rttr.addFlashAttribute("message", "회원가입 완료");
            return "redirect:/loginUserForm";
        } else {
            rttr.addFlashAttribute("error", "회원가입 실패");
            return "redirect:/createUserForm";
        }
    }

    // 로그인 폼
    @GetMapping("loginUserForm")
    public String loginUserForm() {
        return "loginUserForm";
    }

    // 로그인
    @PostMapping("loginUser")
    public String loginUser(
            @ModelAttribute LoginUserDto userDto,
            RedirectAttributes rttr,
            HttpSession session
    ) {
        // 유저 로그인 로직
        User loginUser = userService.loginUser(userDto);

        if(loginUser == null) {
            return "redirect:/loginUserForm";
        } else {
            session.setAttribute("loginUser", loginUser);
            return "redirect:/";
        }
    }

    @GetMapping("logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // 게시판 - 페이징 적용
    @GetMapping("board")
    public String board(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model,
            HttpSession session
    ) {
        List<Board> boards =  boardService.getBoardList();
        model.addAttribute("boards", boards);
        return "board";
    }

    // 글 상세 폼
    @GetMapping("board/{id}")
    public String detailBoard(
            @PathVariable Long id,
            Model model
    ) {
        Board board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "board/detail";
    }

    // 글 작성 폼
    // 글 작성

    // 글 수정 폼
    // 글 수정

    // 글 삭제

}
