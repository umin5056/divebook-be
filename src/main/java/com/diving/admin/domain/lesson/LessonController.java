package com.diving.admin.domain.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<LessonResponse>> getList(
            @AuthenticationPrincipal String instructorId
    ) {
        return ResponseEntity.ok(lessonService.getList(instructorId));
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @AuthenticationPrincipal String instructorId,
            @RequestBody CreateLessonRequest request
    ) {
        String lessonId = lessonService.create(instructorId, request);
        return ResponseEntity.created(URI.create("/api/lessons/" + lessonId)).build();
    }
}
