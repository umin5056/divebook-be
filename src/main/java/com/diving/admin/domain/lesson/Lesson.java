package com.diving.admin.domain.lesson;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_lesson")
@Getter
public class Lesson {

    @Id
    @UuidGenerator
    @Column(name = "lesson_id", length = 36)
    private String lessonId;

    @Column(name = "instructor_id", nullable = false, length = 36)
    private String instructorId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 300)
    private String location;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Column(nullable = false)
    private Short maxStudents;

    @Column(nullable = false)
    private Integer fee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LessonStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;
}
