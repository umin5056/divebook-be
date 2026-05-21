package com.diving.admin.domain.auth;

import com.diving.admin.global.config.FrontendProperties;
import com.diving.admin.global.config.KakaoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final KakaoProperties kakaoProperties;
    private final FrontendProperties frontendProperties;

    @GetMapping("/kakao")
    public ResponseEntity<Void> kakaoLogin() {
        String url = "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + kakaoProperties.clientId()
                + "&redirect_uri=" + kakaoProperties.redirectUri()
                + "&scope=profile_nickname,profile_image";
        return ResponseEntity.status(302).location(Objects.requireNonNull(URI.create(url))).build();
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<Void> kakaoCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error)
    {
        if (error != null) {
            return ResponseEntity.status(302)
                    .location(Objects.requireNonNull(URI.create(frontendProperties.url() + "/")))
                    .build();
        }

        LoginResponse response = authService.kakaoLogin(code);
        String redirect = frontendProperties.url()
                + "/auth/callback"
                + "?accessToken=" + response.accessToken()
                + "&refreshToken=" + response.refreshToken();

        return ResponseEntity.status(302).location(Objects.requireNonNull(URI.create(redirect))).build();
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
