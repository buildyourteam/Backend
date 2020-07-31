# 개요
**프로젝트 이미지 인터페이스** 관련하여 아래 항목을 정의
1. 인터페이스 정의
    - 프로젝트 이미지 관련 인터페이스에 대한 설명
2. API 문서 링크
    - 각 인터페이스에 대한 API 문서 링크
3. 사용 엔터티
    - 인터페이스에서 사용하는 엔터티 리스트

# 클래스 구성
## 1. ProjectImageController
### 정의
웹 서비스 상에서 인증된 프로젝트 이미지 관련 인터페이스 제공  

### 공통 엔터티
- ProfileImage

### 기능 리스트
1. `POST` `/projects/image/{projectid}` [uploadProjectImage(user_id, file)](#1-uploadprojectimagelong-project_id-multipartfile-file) : 프로젝트 사진 업로드
2. `GET` `/projects/image/{projectid}` [downloadProjectImage(project_id, HTTP_request)](#2-downloadprojectimagelong-project_id-httpservletrequest-request) : 프로젝트 사진 가져오기

### 기능 정의
#### 1. uploadProjectImage(Long project_id, MultipartFile file)
  - 프로젝트 이미지 업로드
  - [프로젝트 이미지 업로드](https://egluuapi.codingnome.dev/docs/index.html#resourcesProjectImageUpload "해당 API 문서로 이동")

#### 2. downloadProjectImage(Long project_id, HttpServletRequest request)
  - 프로젝트 이미지 가져오기
  - [프로젝트 이미지 가져오기](https://egluuapi.codingnome.dev/docs/index.html#resourcesProjectImageUpload "해당 API 문서로 이동")


## 2. Service
### 정의
프로젝트 이미지 파일 처리에 대한 비즈니스 로직을 수행

### 공통 레퍼지토리
  - ProfileImageRepository
   
### 기능 리스트
1. [storeProjectImage(Long projectId, MultipartFile file)](#1-storeprojectimagelong-project_id-multipartfile-file) : 프로젝트 이미지 저장
2. [getProjectImage(Long projectId)](#2-getprojectimagelong-project_id) : 프로젝트 이미지 가져오기

### 기능 정의
#### 1. storeProjectImage(Long project_id, MultipartFile file)
  - 프로젝트 이미지 업로드

  - 연관 예외 클래스
    - FileNameException(fileName)
    - FileNameException(fileName, exception)

#### 2. getProjectImage(Long project_id)
  - 프로젝트 이미지 가져오기

  - 연관 예외 클래스
    - ProjectImageNotFoundException(project_id)
