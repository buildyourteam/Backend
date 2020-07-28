# 개요
**프로젝트 상세 정보 인터페이스** 관련하여 아래 항목을 정의
1. 인터페이스 정의
    - 프로젝트 상세 정보 관련 인터페이스에 대한 설명
2. API 문서 링크
    - 각 인터페이스에 대한 API 문서 링크
3. 사용 도메인
    - 인터페이스에서 사용하는 도메인 리스트

# 클래스 구성
## 1. ProjectDetailController
### 정의
웹 서비스 상에서 인증된 사용자에게 특정 프로젝트 정보와 관련된 인터페이스 제공

### 공통 도메인
- －

### 기능 리스트
1. `GET` `/projects/{projectId}` [getProjectDetail()](#1-getprojectdetail) : 프로젝트 상세 정보 조회
2. `GET` `/profile/{userId}/recruit/{projectId}` [getRecruits()](#2-getrecruits) : 프로젝트 영입 제안
3. `POST` `/projects` [createProject()](#3-createproject) : 프로젝트 생성
4. `PUT` `/projects/{project_id} [updateProject()](#4-updateproject) : 프로젝트 수정
5. `DELETE` `/projects/{project_id} [deleteProject()](#5-deleteproject) : 프로젝트 삭제

### 기능 정의
#### 1. getProjectDetail()
  - 인증된 사용자가 특정 프로젝트에 대한 상세 정보를 조회
  - [프로젝트 상세 정보 조회](https://egluuapi.codingnome.dev/docs/index.html#resourcesProjectGet "해당 API 문서로 이동")
  - 연관 엔터티
      - Project

#### 2. getRecruits()
  - 리더가 특정 사용자에게 프로젝트 영입을 제안
  - [프로젝트 영입 제안](https://egluuapi.codingnome.dev/docs/index.html#projectRecruit "해당 API 문서로 이동")
  - 연관 엔터티
      - Recruit

#### 3. createProject()
  - 인증된 사용자가 특정 프로젝트를 생성
  - [프로젝트 생성](https://egluuapi.codingnome.dev/docs/index.html#resourcesProjectCreate "해당 API 문서로 이동")
  - 연관 엔터티
      - Project
                
#### 4. updateProject()
  - 리더 역할을 맡은 사용자가 해당 프로젝트 정보를 수정
  - [프로젝트 수정](https://egluuapi.codingnome.dev/docs/index.html#resourcesProjectUpdate "해당 API 문서로 이동")
  - 연관 엔터티
      - Project
        
#### 5. deleteProject()
  - 리더 역할을 맡은 사용자가 해당 프로젝트를 삭제
  - [프로젝트 삭제](https://egluuapi.codingnome.dev/docs/index.html#resourcesProjectDelete "해당 API 문서로 이동")
  - 연관 엔터티
      - Project
      - ProjectMember
