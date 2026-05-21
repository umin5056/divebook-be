package com.diving.admin;

import com.diving.admin.global.config.FrontendProperties;
import com.diving.admin.global.config.KakaoProperties;
import com.diving.admin.global.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, KakaoProperties.class, FrontendProperties.class})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
