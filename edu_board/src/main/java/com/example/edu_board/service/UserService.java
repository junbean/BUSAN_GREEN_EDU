package com.example.edu_board.service;

import com.example.edu_board.dto.CreateUserDto;
import com.example.edu_board.dto.LoginUserDto;
import com.example.edu_board.entity.User;
import com.example.edu_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 아이디 중복 확인
    public boolean isUsernameDuplicated(String username) {
        return userRepository.existsByUsername(username);
    }

    // 계정 생성
    public boolean createUser(CreateUserDto userDto) {
        try {
            if(isUsernameDuplicated(userDto.getUsername())) {
                System.out.println("duplicate user");
                return false;
            }

            String passwordHash = passwordEncoder.encode(userDto.getPassword());

            User user = User.builder()
                    .username(userDto.getUsername())
                    .password(passwordHash)
                    .name(userDto.getName())
                    .email(userDto.getEmail())
                    .build();

            userRepository.save(user);
            System.out.println("create user");
            return true;
        } catch (Exception e) {
            System.out.println("cannot create user");
            return false;
        }
    }

    // 로그인
    public User loginUser(LoginUserDto userDto) {
        Optional<User> userOptional = userRepository.findByUsername(userDto.getUsername());

        // 유저가 존재하는지 판별
        if(userOptional.isEmpty()) {
            System.out.println("not exist user");
            return null;
        }

        // 유저 실제값 가져오기
        User user = userOptional.get();

        // 비밀번호 판별
        if(passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            System.out.println("login - match password");
            return user;
        } else {
            System.out.println("login - not match password");
            return null;
        }
    }

}
