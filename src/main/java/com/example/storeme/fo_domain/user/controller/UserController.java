package com.example.storeme.fo_domain.user.controller;

import com.example.storeme.fo_domain.user.service.UserService;
import com.example.storeme.global.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 유저 관련 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


}
