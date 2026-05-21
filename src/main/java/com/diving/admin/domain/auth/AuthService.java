package com.diving.admin.domain.auth;

import com.diving.admin.domain.instructor.Instructor;
import com.diving.admin.domain.instructor.InstructorRepository;
import com.diving.admin.global.jwt.JwtProperties;
import com.diving.admin.global.jwt.JwtProvider;
import com.diving.admin.global.redis.RedisTokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoClient kakaoClient;
    private final InstructorRepository instructorRepository;
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final RedisTokenStore redisTokenStore;

    public LoginResponse kakaoLogin(String code) {
        String kakaoAccessToken = kakaoClient.getAccessToken(code);
        KakaoUserInfo userInfo = kakaoClient.getUserInfo(kakaoAccessToken);

        Instructor instructor = instructorRepository.findByKakaoId(userInfo.kakaoId())
                .orElseGet(() -> instructorRepository.save(
                        Instructor.create(userInfo.kakaoId(), userInfo.nickname(), userInfo.profileImageUrl())
                ));

        String instructorId = instructor.getInstructorId();
        String accessToken = jwtProvider.createAccessToken(instructorId);
        String refreshToken = jwtProvider.createRefreshToken(instructorId);

        redisTokenStore.saveRefreshToken(instructorId, refreshToken, jwtProperties.refreshExpiration());

        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refresh(String refreshToken) {
        if (!jwtProvider.isValid(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 refresh token입니다.");
        }

        String instructorId = jwtProvider.getUsername(refreshToken);
        String stored = redisTokenStore.getRefreshToken(instructorId);

        if (!refreshToken.equals(stored)) {
            throw new IllegalArgumentException("refresh token이 일치하지 않습니다.");
        }

        String newAccessToken = jwtProvider.createAccessToken(instructorId);
        String newRefreshToken = jwtProvider.createRefreshToken(instructorId);

        redisTokenStore.saveRefreshToken(instructorId, newRefreshToken, jwtProperties.refreshExpiration());

        return new LoginResponse(newAccessToken, newRefreshToken);
    }

    public void logout(String accessToken) {
        if (!jwtProvider.isValid(accessToken)) {
            throw new IllegalArgumentException("유효하지 않은 access token입니다.");
        }

        String instructorId = jwtProvider.getUsername(accessToken);
        redisTokenStore.deleteRefreshToken(instructorId);
        redisTokenStore.blacklistAccessToken(accessToken, jwtProvider.getRemainingExpiration(accessToken));
    }
}
