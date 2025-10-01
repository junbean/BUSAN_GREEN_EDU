package com.example.edu_board.service;

import com.example.edu_board.dto.BoardDto;
import com.example.edu_board.entity.Board;
import com.example.edu_board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시글 목록 조회 - 리스트
    public List<Board> getBoardList() {
        return boardRepository.findAll();
    }

    // 게시글 목록 조회 - 페이징
    public Page<Board> getBoardPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return boardRepository.findAll(pageable);
    }

    public Board getBoardById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    public boolean createBoard(BoardDto boardDto, String username) {
        try {
            Board board = Board.builder()
                    .title(boardDto.getTitle())
                    .content(boardDto.getContent())
                    .writer(username)
                    .createdAt(LocalDateTime.now())
                    .build();

            boardRepository.save(board);
            System.out.println("create board");
            return true;
        } catch (Exception e) {
            System.out.println("not create board");
            return false;
        }
    }

    public boolean modifyBoard(BoardDto boardDto) {
        try {
            Board board = Board.builder()
                    .id(boardDto.getId())
                    .title(boardDto.getTitle())
                    .content(boardDto.getContent())
                    .writer(boardDto.getWriter())
                    .createdAt(boardDto.getCreatedAt())
                    .build();

            boardRepository.save(board);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteBoard(long id, BoardDto boardDto) {
        boardRepository.deleteById(id);
    }

}
