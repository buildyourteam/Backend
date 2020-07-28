# 개요
**프로젝트 리스트 인터페이스** 관련하여 아래 항목을 정의
1. 인터페이스 정의
    - 프로젝트 리스트 관련 인터페이스에 대한 설명
2. API 문서 링크
    - 각 인터페이스에 대한 API 문서 링크
3. 사용 도메인
    - 인터페이스에서 사용하는 도메인 리스트

# 클래스 구성
## 1. ProjectListController
### 정의
웹 서비스 상에서 일반 사용자에게 프로젝트 리스트 조회 관련 인터페이스 제공

### 공통 엔터티
- ProjectListDto

### 기능 리스트
1. `GET` `/projects` [getProjectsList()](#1-getprojectslist) : 프로젝트 목록 조회
2. `GET` `/projects/{projectId}` [getProjectsDeadline()](#2-getprojectsdeadline) : 프로젝트 영입 제안

### 기능 정의
#### 1. getProjectsList()
  - 일반 사용자가 모든 프로젝트에 대한 리스트를 조회
  - [프로젝트 목록 조회](https://egluuapi.codingnome.dev/docs/index.html#resourcesProjectList "해당 API 문서로 이동")
  - 연관 엔터티
      - －

#### 2. getProjectsDeadline()
  - 일반 사용자가 마감 임박한 프로젝트에 대한 리스트를 조회
  - [프로젝트 영입 제안](https://egluuapi.codingnome.dev/docs/index.html#resourcesDeadlineProjectList "해당 API 문서로 이동")
  - 연관 엔터티
      - －

## 2. Service
### 정의
쭉 하기