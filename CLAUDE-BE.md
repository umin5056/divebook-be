# Backend Skills

## 프로젝트 개요

프리다이빙 강습 관리 어드민 API 서버

## 기술 스택

- **Java** + **Spring Boot**
- **Spring Security** + **JWT** (인증)
- **JPA** / **Hibernate** (ORM)
- **MySQL** (메인 DB)
- **Redis** (캐싱 / 세션)
- **Docker** (컨테이너)
- **AWS** EC2 + RDS + S3 (인프라)
- **GitHub Actions** (CI/CD)

## 프로젝트 구조

```
src/
└── main/
    ├── java/com/diving/admin/
    │   ├── domain/
    │   │   ├── admin/
    │   │   │   ├── Admin.java
    │   │   │   ├── AdminRepository.java
    │   │   │   └── AdminService.java
    │   │   ├── lesson/
    │   │   │   ├── Lesson.java
    │   │   │   ├── LessonRepository.java
    │   │   │   ├── LessonService.java
    │   │   │   └── LessonController.java
    │   │   ├── student/
    │   │   │   ├── Student.java
    │   │   │   ├── StudentRepository.java
    │   │   │   ├── StudentService.java
    │   │   │   └── StudentController.java
    │   │   └── enrollment/
    │   │       ├── LessonEnrollment.java
    │   │       ├── LessonEnrollmentRepository.java
    │   │       ├── LessonEnrollmentService.java
    │   │       └── LessonEnrollmentController.java
    │   ├── global/
    │   │   ├── config/
    │   │   │   ├── SecurityConfig.java
    │   │   │   ├── RedisConfig.java
    │   │   │   └── JwtConfig.java
    │   │   ├── jwt/
    │   │   │   ├── JwtProvider.java
    │   │   │   └── JwtFilter.java
    │   │   └── exception/
    │   │       ├── GlobalExceptionHandler.java
    │   │       └── ErrorResponse.java
    │   └── AdminApplication.java
    └── resources/
        └── application.yml
```

## DB 설계

### Admin (관리자)

```sql

```

### Student (수강생)

```sql

```

### Lesson (수업)

```sql

```

### LessonEnrollment (수업-수강생 배정)

```sql

```

## API 설계

### 인증

```
POST /api/auth/login
POST /api/auth/logout
POST /api/auth/refresh
```

### 수업

```
GET    /api/lessons           # 수업 목록
POST   /api/lessons           # 수업 생성
GET    /api/lessons/:id       # 수업 상세
PUT    /api/lessons/:id       # 수업 수정
DELETE /api/lessons/:id       # 수업 삭제
```

### 수강생

```
GET    /api/students          # 수강생 목록
POST   /api/students          # 수강생 추가
GET    /api/students/:id      # 수강생 상세
PUT    /api/students/:id      # 수강생 수정
DELETE /api/students/:id      # 수강생 삭제
```

### 수업-수강생 배정

```
POST   /api/lessons/:id/enrollments           # 수강생 배정
DELETE /api/lessons/:id/enrollments/:studentId # 배정 취소
PATCH  /api/lessons/:id/enrollments/:studentId # 결제 상태 변경
```

## Redis 사용처

- JWT refresh token 저장 및 만료 관리
- 로그아웃 시 access token 블랙리스트 처리

## 인프라

```
GitHub Push
    ↓
GitHub Actions (CI/CD)
    ↓
Docker Build
    ↓
AWS EC2 배포
    ├── Nginx (리버스 프록시)
    │   ├── / → React 앱 (S3)
    │   └── /api → Spring Boot
    └── RDS (MySQL)
        Redis
```

## application.yml 구조

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate
  redis:
    host: ${REDIS_HOST}
    port: 6379

jwt:
  secret: ${JWT_SECRET}
  access-expiration: 3600000 # 1시간
  refresh-expiration: 604800000 # 7일
```
