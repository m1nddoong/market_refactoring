package com.example.market.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.market.auth.dto.CreateUserDto;
import com.example.market.auth.dto.UserDto;
import com.example.market.auth.entity.User;
import com.example.market.auth.repo.UserRepository;
import com.example.market.auth.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    }

    @InjectMocks
    private UserService userService;


    @Test
    @DisplayName("CreateUserDto로 사용자 생성")
    public void testSignUp() {
        String email = "Brad@naver.com";
        String plainPassword = "1234";
        User userOut = User.builder()
                .email(email)
                .password(plainPassword)
                .build();
        // userRepositroy.save(any()) 의 결과를 userOut 으로 설정
        when(userRepository.save(any()))
                .thenReturn(userOut);

        // when - CreateUserDto 를 전달한다.
        CreateUserDto createUserDto = CreateUserDto.builder()
                .email(email)
                .password(plainPassword)
                .passwordCheck(plainPassword)
                .build();
        UserDto result = userService.signUp(createUserDto);

        assertEquals(email, result.getEmail());
    }
}
