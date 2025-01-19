package com.sparta.backendonboardingassignment.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.backendonboardingassignment.domain.tokens.entity.RefreshToken;
import com.sparta.backendonboardingassignment.domain.tokens.repository.RefreshTokenRepository;
import com.sparta.backendonboardingassignment.domain.users.dto.SignRequestDto;
import com.sparta.backendonboardingassignment.domain.users.entity.User;
import com.sparta.backendonboardingassignment.domain.users.entity.UserRoleEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUnitTest {


    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, refreshTokenRepository);
        jwtAuthenticationFilter.setAuthenticationManager(authentication -> authentication);
    }

    @Test
    @DisplayName("로그인시 Authentication Filter 작동 테스트")
    void testGetAuthentication() throws Exception {
        // given
        String username = "testUser";
        String password = "testPassword";

        String json = "{\"username\":\"testUser\", \"password\":\"testPassword\"}";
        SignRequestDto signRequestDto = objectMapper.readValue(json, SignRequestDto.class);

        // when
        when(request.getInputStream()).thenReturn(new DelegatingServletInputStream(
                new ByteArrayInputStream(objectMapper.writeValueAsBytes(signRequestDto)))
        );
        Authentication result = jwtAuthenticationFilter.attemptAuthentication(request, response);

        // then
        assertNotNull(result);

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) result;

        assertEquals(username, token.getPrincipal());
        assertEquals(password, token.getCredentials());
    }

    @Test
    @DisplayName("로그인 성공시 AccessToken 발급 테스트")
    void testSuccessAuthentication() throws Exception {
        // given
        String username = "testUser";
        String password = "password";
        String nickname = "testNickname";
        String accessToken = "testAccessToken";
        String refreshToken = "testRefreshToken";

        User user = User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .role(UserRoleEnum.USER)
                .build();

        RefreshToken existingToken = new RefreshToken("oldRefreshToken", user);

        // when
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUser()).thenReturn(user);
        when(jwtUtil.createAccessToken(username, UserRoleEnum.USER)).thenReturn(accessToken);
        when(jwtUtil.createRefreshToken(username)).thenReturn(refreshToken);
        when(refreshTokenRepository.findByUserId(user.getId())).thenReturn(Optional.of(existingToken));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(response.getWriter()).thenReturn(new PrintWriter(outputStream));

        jwtAuthenticationFilter.successfulAuthentication(request, response, chain, authentication);

        // then
        String responseBody = outputStream.toString();
        assertTrue(responseBody.contains(accessToken));
    }

}