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
1. `POST` `/profile/{userId}/recruit/{project_id}` [recruitProject()](#1-recruitproject) : 프로젝트 영입하기
2. `GET` `/profile/{userId}/recruit` [getRecruitList()](#2-getrecruitlist) : 프로젝트 영입 리스트 조회
3. `GET` `/profile/{userId}/recruit/{projectId}` [getRecruitProject()](#3-getrecruitproject) : 프로젝트 영입 리스트  조회
4. `PUT` `/profile/{userId}/recruit/{projectId}` [acceptRecruitProject()](#4-acceptrecruitproject) : 프로젝트 영입 승인
5. `DELETE` `/profile/{userId}/recruit/{projectId}` [rejectRecruitProject()](#5-rejectrecruitproject) : 프로젝트 영입 거절

### 기능 정의
#### 1. recruitProject()
  - 프로젝트 리더가 특정 사용자에게 영입을 제안
  - [프로젝트 영입하기](http://34.105.29.115:8080/docs/index.html#projectRecruit "해당 API 문서로 이동")
  - 연관 엔터티
      - Project
      - User
      - Recruit

#### 2. getRecruitList()
  - 인증된 사용자가 프로젝트 리더에게 받은 영입 제안 리스트를 조회
  - [프로젝트 영입 리스트 조회](http://34.105.29.115:8080/docs/index.html#projectRecruit "해당 API 문서로 이동")
  - 연관 엔터티
      - Recruit

#### 3. getRecruitProject()
  - 인증된 사용자가 받은 특정 영입 제안서를 조회
  - 해딩 제안서를 조회 시 해당 제안서는 읽음 상태로 변경
  - [프로젝트 생성](http://34.105.29.115:8080/docs/index.html#getRecruit "해당 API 문서로 이동")
  - 연관 엔터티
      -  Recruit
                
#### 4. acceptRecruitProject()
  - 인증된 사용자가 받은 특정 영입 제안을 승인
  - [프로젝트 영입 승인](http://34.105.29.115:8080/docs/index.html#acceptRecruit "해당 API 문서로 이동")
  - 연관 엔터티
      - Project
        
#### 5. rejectRecruitProject()
  - 인증된 사용자가 받은 튻정 영입 제안을 거절
  - [프로젝트 영입 거절](http://34.105.29.115:8080/docs/index.html#rejectRecruit "해당 API 문서로 이동")
  - 연관 엔터티
      - Recruit
