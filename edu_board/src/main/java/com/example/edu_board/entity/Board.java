package com.example.edu_board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "edu_board")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {
    @Id
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
}
