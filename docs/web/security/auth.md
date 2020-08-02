# 개요
**사용자 인증 인터페이스** 관련하여 아래 항목을 정의
1. 인터페이스 정의
    - 사용자 인증 관련 인터페이스에 대한 설명
2. API 문서 링크
    - 각 인터페이스에 대한 API 문서 링크
3. 사용 엔터티
    - 인터페이스에서 사용하는 엔터티 리스트

# 클래스 구성
## 1. AuthController
### 정의
웹 서비스 상에서 사용자 인증 관련 인터페이스 제공  

### 공통 엔터티
- User

### 기능 리스트
1. `POST` `/auth/signin` [signin(SignInDto)](#1-signin) : 로그인
2. `POST` `/auth/signup` [signup(SignUpDto)](#2-signup) : 회원가입
3. `POST` `/auth/idcheck/{checkId}` [canUseThisId(user_id)](#3-canusethisid) : ID 중복 확인

### 기능 정의
#### 1. signin()
  - 사용자 로그인
  - 로그인 성공 시 응답 헤더에 `JWT 인증 토큰`을 추가
  - [로그인](https://egluuapi.codingnome.dev/docs/index.html#signIn "해당 API 문서로 이동")

#### 2. signup()
  - 사용자 회원가입
  - 패스워드는 `BCrypt 해싱 암호화` 방식을 사용
  - [회원가입](https://egluuapi.codingnome.dev/docs/index.html#SignUp "해당 API 문서로 이동")

#### 3. canUseThisId()
  - 회원가입 시 생성할 ID의 중복 여부 확인
  - [ID 중복 확인](https://egluuapi.codingnome.dev/docs/index.html#SignUp "해당 API 문서로 이동")

## 2. Service
### 정의
사용자 인증 관련 비즈니스 로직을 수행

### 공통 레퍼지토리
  - UserRepository
  
### 기능 리스트
1. [storeProject(ProjectDetailDto, user_id)](#1-signupsignupdto-signupdto) : 회원가입
2. [deleteProject(project_id, visitor_id)](#2-signinsignindto-signindto) : 로그인
3. [updateProject(project_id, UpdateDto, visitor_id)](#3-idcheckstring-checkid) : ID 중복 확인

### 기능 정의
#### 1. signup(SignUpDto signupDto)
  - 신규 사용자로 회원가입

#### 2. signin(SignInDto signInDto)
  - 특정 사용자로 로그인

  - 연관 예외 클래스
    - CUserNotFoundException()
    - CSigninFailedException()
  
#### 3. idCheck(String checkId)
  - 사용자 ID에 대한 중복 확인
