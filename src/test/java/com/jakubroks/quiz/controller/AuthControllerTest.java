package com.jakubroks.quiz.controller;

import com.jakubroks.quiz.dto.RegisterRequest;
import com.jakubroks.quiz.entity.User;
import com.jakubroks.quiz.filter.AuthFilter;
import com.jakubroks.quiz.service.AuthService;
import com.jakubroks.quiz.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private AuthFilter authFilter;

    @Test
    void givenValidRegisterRequest_whenRegisteringUser_thenReturnsCreatedUser() {
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setUsername("jan");
        createdUser.setEmail("janxsx50@example.com");
        createdUser.setPassword("password");
        createdUser.setRole("USER");

        when(userService.register(any(RegisterRequest.class))).thenReturn(createdUser);

        var result = mockMvc.post().uri("/register")
                .contentType("application/json")
                .content("""
                {
                  "username": "jan",
                  "email": "janxsx50@example.com",
                  "password": "password"
                }
                """);


        assertThat(result)
                .hasStatus(201);

        assertThat(result)
                .bodyJson()
                .extractingPath("$.id").isEqualTo(1);

        assertThat(result)
                .bodyJson()
                .extractingPath("$.username").isEqualTo("jan");

        assertThat(result)
                .bodyJson()
                .extractingPath("$.email").isEqualTo("janxsx50@example.com");

        assertThat(result)
                .bodyJson()
                .extractingPath("$.role").isEqualTo("USER");

    }


    @Test
    void givenValidCredentials_whenAuthenticating_thenReturnsAuthKey() {
        when(userService.authenticate("jan@email.com", "password"))
                .thenReturn("test-auth-key");

        var result = mockMvc.post().uri("/auth")
                .contentType("application/json")
                .content("""
                    {
                      "email": "jan@email.com",
                      "password": "password"
                    }
                    """);

        assertThat(result)
                .hasStatusOk();

        assertThat(result)
                .bodyJson()
                .extractingPath("$.key").isEqualTo("test-auth-key");

    }

    @Test
    void givenInvalidJson_whenAuthenticating_thenReturnsBadRequest() {
        var result = mockMvc.post().uri("/auth")
                .contentType("application/json")
                .content("""
                    {
                      "email": "jan@email.com",
                      "password": "", 
                    }
                    """);

        assertThat(result)
                .hasStatus(HttpStatus.BAD_REQUEST);
    }
}
