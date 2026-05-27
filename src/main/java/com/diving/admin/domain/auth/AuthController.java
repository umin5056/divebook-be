package com.diving.admin.domain.auth;

import com.diving.admin.global.config.KakaoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@SuppressWarnings("null")
public class AuthController {

    private final AuthService authService;
    private final KakaoProperties kakaoProperties;

    @Value("${frontend.url}")
    private String frontendUrl;

    @GetMapping("/kakao")
    public ResponseEntity<Void> kakaoLogin() {
        String url = "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + kakaoProperties.clientId()
                + "&redirect_uri=" + kakaoProperties.redirectUri()
                + "&scope=profile_nickname,profile_image";

        return ResponseEntity.status(302).location(URI.create(url)).build();
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<Void> kakaoCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error)
    {
        if (error != null) {
            return ResponseEntity.status(302)
                    .location(URI.create(frontendUrl + "/"))
                    .build();
        }

        LoginResponse response = authService.kakaoLogin(code);
        String redirect = frontendUrl
                + "/auth/callback"
                + "?accessToken=" + response.accessToken()
                + "&refreshToken=" + response.refreshToken();
        

        return ResponseEntity.status(302).location(URI.create(redirect)).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String bearer) {
        String accessToken = bearer.substring(7);
        authService.logout(accessToken);
        return ResponseEntity.noContent().build();
    }
}
