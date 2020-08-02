# 개요
**구인 인터페이스** 관련하여 아래 항목을 정의
1. 인터페이스 정의
    - 구인 관련 인터페이스에 대한 설명
2. API 문서 링크
    - 각 인터페이스에 대한 API 문서 링크
3. 사용 도메인
    - 인터페이스에서 사용하는 도메인 리스트

# 클래스 구성
## 1. PeopleController
### 정의
웹 서비스 상에서 인증된 사용자에게 프로젝트 구인 관련 인터페이스 제공  

### 공통 엔터티
- －

### 기능 리스트
1. `GET` `/people` [getJobSeekers(pageable, assembler, grade, role, area)](#1-getjobseekers) : 구인자 목록 조회

### 기능 정의
#### 1. getJobSeekers()
  - 구인 중인 사용자 리스트를 조회
  - 각 사용자에 대한 `이름, 기술스택, 지역, 레벨, 역할`로 리스트 구성
  - [구인자 목록 조회](https://egluuapi.codingnome.dev/docs/index.html#resourcesPeople "해당 API 문서로 이동")
  - 연관 엔터티
      - User
      - UsersStack

## 2. Service
### 정의
프로젝트 리스트 조회 관련 비즈니스 로직을 수행

### 기능 리스트
1. [getPeople(grade, ProjectRole, area, Pageable)](#1-getpeoplelong-grade-projectrole-role-string-area-pageable-pageable) : 사용자 정보 조회

### 기능 정의
#### 1. getPeople(Long grade, ProjectRole role, String area, Pageable pageable)
  - 레벨, 프로젝트 역할, 지역에 따른 사용자 정보를 리스트로 조회
  - 연관 레퍼지토리
    - UserRepository
