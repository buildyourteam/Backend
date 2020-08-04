# 개요
**사용자 프로필 인터페이스** 관련하여 아래 항목을 정의
1. 인터페이스 정의
    - 사용자 프로필 관련 인터페이스에 대한 설명
2. API 문서 링크
    - 각 인터페이스에 대한 API 문서 링크
3. 사용 도메인
    - 인터페이스에서 사용하는 도메인 리스트

# 클래스 구성
## 1. ProfileController
### 정의
웹 서비스 상에서 인증된 사용자에 대한 프로필 관련 인터페이스 제공  

### 기능 리스트
1. `GET` `/profile/{userId}` [getProfile(user_id)](#1-getprofile) : 사용자 프로필 조회
2. `PUT` `/profile/{userId}` [updateProfile(user_id, ProfileDto)](#2-updateprofile) : 사용자 프로필 수정
3. `GET` `/profile/{user_id}/running` [getRunningProjects(user_id, pageable, assembler)](#3-getrunningprojects) : 사용자가 참여 중인 프로젝트 조회
4. `GET` `/profile/{user_id}/ended` [getEndedProjects(user_id, pageable, assembler)](#4-getendedprojects) : 사용자가 참여했던 프로젝트 조회
5. `GET` `/profile/{user_id}/plan` [getMyPlanProjects(user_id, pageable, assembler)](#5-getmyplanprojects) : 사용자가 기획한 프로젝트 조회
6. `GET` `/profile/{user_id}/running/hidden` [getRunningHiddenProjects(user_id, pageable, assembler)](#6-getrunninghiddenprojects) : 사용자가 참여 중인 프로젝트 조회 (숨겨진 프로젝트)
7. `GET` `/profile/{user_id}/ended/hidden` [getEndedHiddenProjects(user_id, pageable, assembler)](#7-getendedhiddenprojects) : 사용자가 참여했던 프로젝트 조회 (숨겨진 프로젝트)
8. `GET` `/profile/{user_id}/plan/hidden` [getRunningHiddenProjects(user_id, pageable, assembler)](#8-getmyplanhiddenprojects) : 사용자가 기획한 프로젝트 조회 (숨겨진 프로젝트)
9. `DELETE` `/profile/{user_id}/plan/hidden` [hideProject(user_id, project_id, pageable, assembler)](#9-hideproject) : 프로젝트 숨기기

### 기능 정의
#### 1. getProfile()
  - 사용자의 프로필 가져오기
  - [프로필 데이터 조회](https://egluuapi.codingnome.dev/docs/index.html#resourcesProfileGet "해당 API 문서로 이동")
  - 연관 엔터티
      - User
      
#### 2. updateProfile()
  - 사용자의 프로필 수정
  - [프로필 수정](https://egluuapi.codingnome.dev/docs/index.html#resourcesProfileUpdate "해당 API 문서로 이동")
  - 연관 엔터티
      - User

#### 3. getRunningProjects()
  - 사용자가 현재 참여 중인 프로젝트 리스트 가져오기
  - [현재 참여 중인 프로젝트 리스트 조회](https://egluuapi.codingnome.dev/docs/index.html#resourcesRunningProjectList "해당 API 문서로 이동")

#### 4. getEndedProjects()
  - 사용자가 참여했던 프로젝트 리스트 가져오기
  - [참여했던 프로젝트 리스트 조회](https://egluuapi.codingnome.dev/docs/index.html#resourcesEndedProjectList "해당 API 문서로 이동")

#### 5. getMyPlanProjects()
  - 사용자가 기획한 프로젝트 리스트 가져오기
  - [기획한 프로젝트 리스트 조회](https://egluuapi.codingnome.dev/docs/index.html#resourcesPlannedProjectList "해당 API 문서로 이동")

#### 6. getRunningHiddenProjects()
  - 인증된 사용자가 참여 중인 프로젝트 리스트 가져오기
  - 위 데이터는 해당 사용자가 `숨기기 기능을 활성화`한 리스트
  - [참여 중인 프로젝트 리스트 조회](https://egluuapi.codingnome.dev/docs/index.html#resourcesRunningHiddenProjectList "해당 API 문서로 이동")

#### 7. getEndedHiddenProjects()
  - 인증된 사용자가 참여했던 프로젝트 리스트 가져오기
  - 위 데이터는 해당 사용자가 `숨기기 기능을 활성화`한 리스트
  - [참여했던 프로젝트 리스트 조회](https://egluuapi.codingnome.dev/docs/index.html#resourcesEndedProjectList "해당 API 문서로 이동")

#### 8. getMyPlanHiddenProjects()
  - 인증된 사용자가 기획한 프로젝트 리스트 가져오기
  - 위 데이터는 해당 사용자가 `숨기기 기능을 활성화`한 리스트
  - [사용자가 기획한 프로젝트 리스트 조회](https://egluuapi.codingnome.dev/docs/index.html#resourcesPlannedHiddenProjectList "해당 API 문서로 이동")

#### 9. hideProject()
  - 인증된 사용자가 숨기기 기능을 활성화
  - [프로젝트 숨기기 기능 활성화](https://egluuapi.codingnome.dev/docs/index.html#hideProject "해당 API 문서로 이동")
  - 연관 엔터티
      - Project
      - ProjectMember

## 2. Service
### 정의
사용자 프로필 관련 서비스에 대한 비즈니스 로직을 수행

### 기능 리스트
1. [getProfile(user_id)](#1-getprofilestring-user_id) : 특정 프로필 조회
2. [updateProfile(user_id, visitor_id, ProfileDto)](#2-updateprofilestring-user_id-string-visitor_id-profiledto-updatedata) : 프로필 수정
3. [getRunning(user_id, Pageable)](#3-getrunningstring-user_id-pageable-pageable) : 진행 중인 프로젝트 리스트 조회
4. [getEnded(user_id, Pageable)](#4-getendedstring-user_id-pageable-pageable) : 종료된 프로젝트 리스트 조회
5. [getPlanner(user_id, Pageable)](#5-getplannerstring-user_id-pageable-pageable) : 기획 중인 프로젝트 리스트 조회
6. [getHiddenRunning(user_id, visitor_id, Pageable)](#6-gethiddenrunningstring-user_id-string-visitor_id-pageable-pageable) : 진행 중인 프로젝트 리스트 조회 (숨겨진 프로젝트)
7. [getHiddenEnded(user_id, visitor_id, Pageable)](#7-gethiddenendedstring-user_id-string-visitor_id-pageable-pageable) : 종료된 프로젝트 리스트 조회 (숨겨진 프로젝트)
8. [getHiddenPlanner(user_id, visitor_id, Pageable)](#8-gethiddenplannerstring-user_id-string-visitor_id-pageable-pageable) : 기획 중인 프로젝트 리스트 조회 (숨겨진 프로젝트)
9. [reShowProject(user_id, visitor_id, project_id)](#9-reshowprojectstring-user_id-string-visitor_id-long-project_id) : 숨긴 프로젝트를 다시 보여주기
10. [hideProject(user_id, visitor_id, project_id)](#10-hideprojectstring-user_id-string-visitor_id-long-project_id) : 프로젝트 숨기기

### 기능 정의
#### 1. getProfile(String user_id)
  - 현재 활동 중인 사용자의 프로필 조회

  - 연관 예외 클래스
    - UserNotFoundException(user_id)
    
  - 연관 레퍼지토리
    - UserRepository
    
#### 2. updateProfile(String user_id, String visitor_id, ProfileDto updateData)
  - 자신의 프로필에 대한 데이터 수정

  - 연관 예외 클래스
    - NotYourProfileException(user_id)
    - UserNotFoundException(user_id)

  - 연관 레퍼지토리
    - UserRepository

#### 3. getRunning(String user_id, Pageable pageable)
  - 특정 사용자가 현재 참여 중인 프로젝트 리스트 가져오기
  - 조회 중인 프로필에 해당하는 사용자가 숨긴 프로젝트 항목은 조회 불가

  - 연관 레퍼지토리
    - ProjectRepository
 
#### 4. getEnded(String user_id, Pageable pageable)
  - 특정 사용자가 끝낸 프로젝트에 대한 리스트 가져오기
  - 프로필에 해당하는 사용자가 숨긴 프로젝트 항목은 조회 불가

  - 연관 레퍼지토리
    - ProjectRepository

#### 5. getPlanner(String user_id, Pageable pageable)
  - 특정 사용자가 기획 중인 프로젝트 리스트 가져오기

  - 연관 레퍼지토리
    - ProjectRepository
    
#### 6. getHiddenRunning(String user_id, String visitor_id, Pageable pageable)
  - 인증된 사용자가 참여 중인 프로젝트 리스트 가져오기
  - 프로필에 해당하는 사용자가 `숨기기 기능을 활성화`한 리스트

  - 연관 예외 클래스
    - NotYourProfileException(user_id)

  - 연관 레퍼지토리
    - ProjectRepository

#### 7. getHiddenEnded(String user_id, String visitor_id, Pageable pageable)
  - 인증된 사용자가 끝낸 프로젝트 리스트 가져오기
  - 프로필에 해당하는 사용자가 `숨기기 기능을 활성화`한 리스트

  - 연관 예외 클래스
    - NotYourProfileException(user_id)

  - 연관 레퍼지토리
    - ProjectRepository

#### 8. getHiddenPlanner(String user_id, String visitor_id, Pageable pageable)
  - 인증된 사용자가 기획 중인 프로젝트 리스트 가져오기
  - 프로필에 해당하는 사용자가 `숨기기 기능을 활성화`한 리스트

  - 연관 예외 클래스
    - NotYourProfileException(user_id)

  - 연관 레퍼지토리
    - ProjectRepository

#### 9. reShowProject(String user_id, String visitor_id, Long project_id)
  - 프로젝트 숨기기 기능 disable
  
  - 연관 예외 클래스
    - NotYourProfileException(user_id)
    - ProjectNotFoundException(project_id)
    - YouAreNotMemberException(project_id)

  - 연관 레퍼지토리
    - ProjectRepository
    - ProjectMemberRepository

#### 10. hideProject(String user_id, String visitor_id, Long project_id)
  - 프로젝트 숨기기 기능 enable

  - 연관 예외 클래스
    - NotYourProfileException(user_id)
    - ProjectNotFoundException(project_id)
    - YouAreNotMemberException(project_id)

  - 연관 레퍼지토리
    - ProjectRepository
    - ProjectMemberRepository  