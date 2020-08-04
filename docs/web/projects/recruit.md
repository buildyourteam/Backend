# 개요
**프로젝트 영입 인터페이스** 관련하여 아래 항목을 정의
1. 인터페이스 정의
    - 프로젝트 영입 관련 인터페이스에 대한 설명
2. API 문서 링크
    - 각 인터페이스에 대한 API 문서 링크
3. 사용 도메인
    - 인터페이스에서 사용하는 도메인 리스트

# 클래스 구성
## 1. RecruitController
### 정의
웹 서비스 상에서 인증된 사용자에게 제공하는 영입 관련 인터페이스 제공

### 공통 도메인
- －

### 기능 리스트
1. `POST` `/profile/{userId}/recruit/{project_id}` [recruitProject(user_id, project_id, RecruitDto)](#1-recruitproject) : 프로젝트 영입하기
2. `GET` `/profile/{userId}/recruit` [getRecruitList(user_id)](#2-getrecruitlist) : 프로젝트 영입 리스트 조회
3. `GET` `/profile/{userId}/recruit/{projectId}` [getRecruitProject(user_id, project_id)](#3-getrecruitproject) : 프로젝트 영입 제안 확인
4. `PUT` `/profile/{userId}/recruit/{projectId}` [acceptRecruitProject(user_id, project_id)](#4-acceptrecruitproject) : 프로젝트 영입 승인
5. `DELETE` `/profile/{userId}/recruit/{projectId}` [rejectRecruitProject(user_id, project_id)](#5-rejectrecruitproject) : 프로젝트 영입 거절

### 기능 정의
#### 1. recruitProject()
  - 프로젝트 리더가 특정 사용자에게 영입을 제안
  - [프로젝트 영입하기](https://egluuapi.codingnome.dev/docs/index.html#projectRecruit "해당 API 문서로 이동")
  - 연관 엔터티
      - Project
      - User
      - Recruit

#### 2. getRecruitList()
  - 사용자가 프로젝트 리더에게 받은 영입 제안 리스트를 조회
  - [프로젝트 영입 리스트 조회](https://egluuapi.codingnome.dev/docs/index.html#projectRecruit "해당 API 문서로 이동")
  - 연관 엔터티
      - Recruit

#### 3. getRecruitProject()
  - 사용자가 받은 특정 영입 제안서를 조회
  - 해딩 제안서를 조회 시 해당 제안서는 읽음 상태로 변경
  - [프로젝트 생성](https://egluuapi.codingnome.dev/docs/index.html#getRecruit "해당 API 문서로 이동")
  - 연관 엔터티
      -  Recruit
                
#### 4. acceptRecruitProject()
  - 인증된 사용자가 받은 특정 영입 제안을 승인
  - [프로젝트 영입 승인](https://egluuapi.codingnome.dev/docs/index.html#acceptRecruit "해당 API 문서로 이동")
  - 연관 엔터티
      - Project
        
#### 5. rejectRecruitProject()
  - 인증된 사용자가 받은 특정 영입 제안을 거절
  - [프로젝트 영입 거절](https://egluuapi.codingnome.dev/docs/index.html#rejectRecruit "해당 API 문서로 이동")
  - 연관 엔터티
      - Recruit

## 2. Service
### 정의
프로젝트 구인 관련 서비스에 대한 비즈니스 로직을 수행

### 공통 레퍼지토리
  - ProjectRepository
  
### 기능 리스트
1. [recruitProject(user_id, RecruitDto, visitor_id)](#1-recruitprojectstring-user_id-recruitdto-recruit-string-visitor_id) : 영입 제안
2. [getRecruitList(user_id, visitor_id)](#2-getrecruitliststring-user_id-string-visitor_id) : 받은 영입 제안 리스트 조회
3. [getRecruit(user_id, project_id, visitor_id)](#3-getrecruitstring-user_id-long-project_id-string-visitor_id) : 받은 영입 제안서 조회
4. [acceptRecruit(user_id, project_id, visitor_id)](#4-acceptrecruitstring-user_id-long-project_id-string-visitor_id) : 영입 제안 수락
5. [rejectRecruit(user_id, project_id, visitor_id)](#5-rejectrecruitstring-user_id-long-project_id-string-visitor_id) : 영입 제안 거절

### 기능 정의
#### 1. recruitProject(String user_id, RecruitDto recruit, String visitor_id)
  - 특정 사용자에게 영입 제안

  - 연관 예외 클래스
    - ProjectNotFoundException(recruit.getProjectId())
    - YouAreNotLeaderException(visitor_id)
    - UserNotFoundException(user_id)
    
  - 연관 레퍼지토리
    - ProjectRepository
    - UserRepository
    - RecruitRepository
    
#### 2. getRecruitList(String user_id, String visitor_id)
  - 사용자가 받은 영입 제안에 대한 리스트를 조회

  - 연관 예외 클래스
    - RecruitNotAuthException()

  - 연관 레퍼지토리
    - RecruitRepository
  
#### 3. getRecruit(String user_id, Long project_id, String visitor_id)
  - 나에게 온 영입 제안서 조회
  - 사용자가 최초 조회 시 제안서 상태를 '읽음'으로 변경
  
  - 연관 예외 클래스
    - RecruitNotAuthException()
    - RecruitNotFoundException()
 
  - 연관 레퍼지토리
    - RecruitRepository

#### 4. acceptRecruit(String user_id, Long project_id, String visitor_id)
  - 사용자가 받은 영입 제안을 수락

  - 연관 예외 클래스
    - RecruitNotAuthException()
    - RecruitNotFoundException()
 
  - 연관 레퍼지토리
    - RecruitRepository

#### 5. rejectRecruit(String user_id, Long project_id, String visitor_id)
  - 사용자가 받은 영입 제안을 거절

  - 연관 예외 클래스
    - RecruitNotAuthException()
    - RecruitNotFoundException()
 
  - 연관 레퍼지토리
    - RecruitRepository
    