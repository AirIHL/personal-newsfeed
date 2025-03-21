# 뉴스피드 복습 개인 과제

**Personal Newsfeed**는 사용자가 게시글을 작성하고, 팔로우한 사용자의 게시글을 뉴스피드에서 확인하며, 댓글과 좋아요를 통해 소셜 네트워크를 즐길 수 있는 REST API 기반 애플리케이션입니다. Spring Boot와 JPA를 활용해 구현되었으며, JWT를 사용한 인증/인가를 지원합니다.

---

## 프로젝트 개요

이 프로젝트는 개인 뉴스피드 서비스를 제공하는 백엔드 애플리케이션으로, 사용자가 게시글과 댓글을 작성하고, 다른 사용자를 팔로우하며, 좋아요를 통해 상호작용할 수 있는 기능을 제공합니다. 주요 기능은 다음과 같습니다:

- **게시글 관리**: 게시글 생성, 조회(단건/다건), 수정, 삭제, 뉴스피드 조회.
- **댓글 관리**: 댓글 생성, 조회, 수정, 삭제.
- **좋아요 기능**: 게시글 및 댓글에 좋아요 등록/취소.
- **팔로우 기능**: 사용자 팔로우, 언팔로우, 팔로잉 목록 조회.
- **회원 관리**: 회원가입, 로그인, 프로필 조회/수정, 비밀번호 변경.
- **인증/인가**: JWT 기반 인증 및 권한 관리.

---

## 주요 기능

### 1. 게시글 (Article)

- 게시글 생성, 수정, 삭제.
- 전체 게시글 조회 (페이징, 날짜 필터링, 정렬 지원).
- 단건 게시글 조회.
- 뉴스피드: 팔로우한 사용자의 게시글 조회 (페이징 지원).
- 게시글 좋아요 등록/취소.

### 2. 댓글 (Comment)

- 게시글에 댓글 생성, 수정, 삭제.
- 게시글별 댓글 목록 조회 (페이징 지원).
- 댓글 좋아요 등록/취소.

### 3. 팔로우 (Follow)

- 사용자 팔로우/언팔로우.
- 팔로잉 목록 조회.

### 4. 회원 (Member)

- 회원가입 및 로그인 (JWT 발급).
- 본인 프로필 조회 및 수정.
- 비밀번호 변경.
- 전체 회원 목록 조회 및 특정 회원 조회.

### 5. 인증/인가

- JWT를 사용한 인증.
- `@Auth` 어노테이션을 통해 인증된 사용자 정보 주입.
- 권한 체크: 게시글/댓글 수정/삭제는 작성자 본인만 가능.

---




## API 명세서

![API 명세서](https://github.com/AirIHL/personal-newsfeed/blob/main/API%20%EB%AA%85%EC%84%B8%EC%84%9C.png)





## ERD

![ERD](https://github.com/AirIHL/personal-newsfeed/blob/main/ERD.png)





## API 동작



### 회원가입

![회원가입 API](https://github.com/AirIHL/personal-newsfeed/blob/main/%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85%20API.png)



### 로그인

![로그인 API](https://github.com/AirIHL/personal-newsfeed/blob/main/%EB%A1%9C%EA%B7%B8%EC%9D%B8%20API.png)



### 본인 프로필 조회

![본인 프로필 조회 API](https://github.com/AirIHL/personal-newsfeed/blob/main/%EB%B3%B8%EC%9D%B8%20%ED%94%84%EB%A1%9C%ED%95%84%20%EC%A1%B0%ED%9A%8C%20API.png)



### 타인 프로필 조회

![타인 프로필 조회 API](https://github.com/AirIHL/personal-newsfeed/blob/main/%ED%83%80%EC%9D%B8%20%ED%94%84%EB%A1%9C%ED%95%84%20%EC%A1%B0%ED%9A%8C%20API.png)



### 프로필 업데이트
![프로필 업데이트 API](https://github.com/AirIHL/personal-newsfeed/blob/main/%ED%94%84%EB%A1%9C%ED%95%84%20%EC%97%85%EB%8D%B0%EC%9D%B4%ED%8A%B8%20%20API.png)



### 비밀번호 업데이트

![비밀번호 업데이트 API](https://github.com/AirIHL/personal-newsfeed/blob/main/%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8%20%EC%97%85%EB%8D%B0%EC%9D%B4%ED%8A%B8%20%20API.png)

