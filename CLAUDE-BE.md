# Backend Skills

## 프로젝트 개요

프리다이빙 강습 관리 어드민 API 서버

## 기술 스택

- **Java** + **Spring Boot**
- **Spring Security** + **JWT** (인증)
- **JPA** / **Hibernate** (ORM)
- **MariaDB** (메인 DB)
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
    │   │   ├── crew/
    │   │   ├── instructor/
    │   │   ├── student/
    │   │   ├── lesson/
    │   │   └── enrollment/
    │   ├── global/
    │   │   ├── config/
    │   │   │   └── SecurityConfig.java
    │   │   ├── jwt/
    │   │   │   ├── JwtProvider.java
    │   │   │   ├── JwtFilter.java
    │   │   │   └── JwtProperties.java
    │   │   └── exception/
    │   │       ├── GlobalExceptionHandler.java
    │   │       └── ErrorResponse.java
    │   └── AdminApplication.java
    └── resources/
        ├── application.yml
        └── application-db.yml
```

## DB 설계

### tbl_crew (크루)

| 컬럼 | 타입 | 설명 |
|---|---|---|
| crew_id | char(36) | PK, UUID |
| name | varchar(100) | 크루명 |
| description | varchar(500) | 설명 |
| created_at | datetime | 생성일 |
| modified_at | datetime | 수정일 |

### tbl_instructor (강사)

| 컬럼 | 타입 | 설명 |
|---|---|---|
| instructor_id | char(36) | PK, UUID |
| crew_id | char(36) | FK → tbl_crew |
| kakao_id | varchar(100) | 카카오 ID (UNI) |
| email | varchar(255) | 이메일 |
| phone | varchar(20) | 전화번호 |
| profile_image_url | varchar(500) | 프로필 이미지 |
| name | varchar(100) | 이름 |
| memo | text | 메모 |
| created_at | datetime | 생성일 |
| modified_at | datetime | 수정일 |

### tbl_student (수강생)

| 컬럼 | 타입 | 설명 |
|---|---|---|
| student_id | char(36) | PK, UUID |
| crew_id | char(36) | FK → tbl_crew |
| phone | varchar(20) | 전화번호 (UNI) |
| name | varchar(100) | 이름 |
| email | varchar(255) | 이메일 |
| memo | text | 메모 |
| created_at | datetime | 생성일 |
| modified_at | datetime | 수정일 |

### tbl_lesson (수업)

| 컬럼 | 타입 | 설명 |
|---|---|---|
| lesson_id | char(36) | PK, UUID |
| instructor_id | char(36) | FK → tbl_instructor |
| title | varchar(200) | 수업명 |
| location | varchar(300) | 장소 |
| lesson_date | date | 수업 날짜 |
| start_time | time | 시작 시간 |
| end_time | time | 종료 시간 |
| max_students | smallint | 최대 수강 인원 |
| fee | int | 수강료 |
| memo | text | 메모 |
| status | enum | open / closed / cancelled |
| created_at | datetime | 생성일 |
| modified_at | datetime | 수정일 |

### tbl_enrollment (수업-수강생 배정)

| 컬럼 | 타입 | 설명 |
|---|---|---|
| enrollment_id | char(36) | PK, UUID |
| lesson_id | char(36) | FK → tbl_lesson |
| student_id | char(36) | FK → tbl_student |
| payment_status | enum | pending / paid / refunded |
| requested_at | datetime | 배정일 |
| modified_at | datetime | 수정일 |

## API 설계

### 인증

```
POST /api/auth/login
POST /api/auth/logout
POST /api/auth/refresh
```

### 크루

```
GET    /api/crews           # 크루 목록
POST   /api/crews           # 크루 생성
GET    /api/crews/:id       # 크루 상세
PUT    /api/crews/:id       # 크루 수정
DELETE /api/crews/:id       # 크루 삭제
```

### 강사

```
GET    /api/instructors           # 강사 목록
POST   /api/instructors           # 강사 추가
GET    /api/instructors/:id       # 강사 상세
PUT    /api/instructors/:id       # 강사 수정
DELETE /api/instructors/:id       # 강사 삭제
```

### 수강생

```
GET    /api/students           # 수강생 목록
POST   /api/students           # 수강생 추가
GET    /api/students/:id       # 수강생 상세
PUT    /api/students/:id       # 수강생 수정
DELETE /api/students/:id       # 수강생 삭제
```

### 수업

```
GET    /api/lessons           # 수업 목록
POST   /api/lessons           # 수업 생성
GET    /api/lessons/:id       # 수업 상세
PUT    /api/lessons/:id       # 수업 수정
DELETE /api/lessons/:id       # 수업 삭제
```

### 수업-수강생 배정

```
POST   /api/lessons/:id/enrollments              # 수강생 배정
DELETE /api/lessons/:id/enrollments/:studentId   # 배정 취소
PATCH  /api/lessons/:id/enrollments/:studentId   # 결제 상태 변경
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
    └── RDS (MariaDB)
        Redis
```
