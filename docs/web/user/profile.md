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

### 공통 엔터티
- －

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
쭉 하기