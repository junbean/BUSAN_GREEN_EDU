package com.example.edu_board.controller;

import com.example.edu_board.dto.BoardDto;
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

    // 메인 화면 - 게시판
    @GetMapping("/")
    public String home(Model model) {
        List<Board> boardList =  boardService.getBoardList();
        model.addAttribute("boardList", boardList);
        return "boardList";
    }

    // 글 상세 폼
    @GetMapping("/{id}")
    public String boardDetail(@PathVariable Long id, Model model) {
        Board board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "boardDetail";
    }

    // 글 작성 폼
    @GetMapping("/writeForm")
    public String boardWriteForm(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");

        if(loginUser == null) {
            System.out.println("not login user");
            return "redirect:/";
        }

        model.addAttribute("username", loginUser.getUsername());
        return "boardWrite";
    }

    // 글 작성
    @PostMapping("/write")
    public String boardWrite(
            @ModelAttribute BoardDto boardDto,
            HttpSession session
    ) {
        User loginUser = (User) session.getAttribute("loginUser");

        if(loginUser == null) {
            System.out.println("not login user");
            return "redirect:/";
        }

        boolean isCreate = boardService.createBoard(boardDto, loginUser.getUsername());
        if(isCreate) {
            return "redirect:/";
        } else {
            return "redirect:/writeForm";
        }
    }

    // 글 수정 폼
    @GetMapping("/modify/{id}")
    public String boardModifyForm(
            @PathVariable Long id,
            HttpSession session,
            Model model
    ) {
        User loginUser = (User) session.getAttribute("loginUser");

        if(loginUser == null) {
            System.out.println("not login user");
            return "redirect:/";
        }

        Board board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "boardModify";
    }
    // 글 수정
    @PostMapping("/modify/{id}")
    public String boardModify(
            @PathVariable Long id,
            @ModelAttribute BoardDto boardDto,
            HttpSession session
    ) {
        User loginUser = (User) session.getAttribute("loginUser");

        if(loginUser == null) {
            System.out.println("not login user");
            return "redirect:/";
        }

        boolean isModify = boardService.modifyBoard(boardDto);
        if(isModify) {
            return "redirect:/";
        } else {
            return "redirect:/modify/" + boardDto.getId();
        }
    }

    // 글 삭제

}

/*

-- 사용자 테이블 : edu_board_user
-- 컬럼 : username, password, name, email, createdAt ... 이정도?
CREATE TABLE edu_board_user (
	username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
	PRIMARY KEY (username)
);

select * from edu_board_user;


-- 게시글 테이블 : edu_board
-- 컬럼 : id, title, content, writer, createdAt
CREATE TABLE edu_board (
	id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    writer VARCHAR(100) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id),
    CONSTRAINT fk_edu_board_writer FOREIGN KEY(writer) REFERENCES edu_board_user(username) ON DELETE CASCADE
);

select * from edu_board;

commit;

 */