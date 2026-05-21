package com.diving.admin.domain.auth;

public record KakaoUserInfo(
        String kakaoId,
        String nickname,
        String profileImageUrl
) {}
