package com.diving.admin.domain.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    public List<LessonResponse> getList(String instructorId) {
        return lessonRepository.findByInstructorIdOrderByLessonDateDesc(instructorId)
                .stream().map(LessonResponse::from).toList();
    }

    public String create(String instructorId, CreateLessonRequest request) {
        Lesson lesson = Lesson.create(instructorId, request);
        return lessonRepository.save(lesson).getLessonId();
    }
}
