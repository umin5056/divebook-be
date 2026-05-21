package com.diving.admin.domain.enrollment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
}
