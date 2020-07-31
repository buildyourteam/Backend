# 개요
**프로필 이미지 인터페이스** 관련하여 아래 항목을 정의
1. 인터페이스 정의
    - 프로필 이미지 관련 인터페이스에 대한 설명
2. API 문서 링크
    - 각 인터페이스에 대한 API 문서 링크
3. 사용 엔터티
    - 인터페이스에서 사용하는 엔터티 리스트

# 클래스 구성
## 1. ProfileImageController
### 정의
웹 서비스 상에서 인증된 사용자 프로필 이미지 관련 인터페이스 제공  

### 공통 엔터티
- ProfileImage

### 기능 리스트
1. `POST` `/profile/image/{user_id}` [uploadProfileImage(user_id, file)](#1-uploadprofileimagestring-user_id-multipartfile-file) : 프로필 사진 업로드
2. `GET` `/profile/image/{user_id}` [downloadProfileImage(user_id, HTTP_request)](#2-downloadprofileimagestring-user_id-httpservletrequest-request) : 프로필 사진 가져오기

### 기능 정의
#### 1. uploadProfileImage(String user_id, MultipartFile file)
  - 인증된 사용자의 프로필 이미지 업로드
  - [프로필 이미지 업로드](https://egluuapi.codingnome.dev//docs/index.html#resourcesProfileImageUpload "해당 API 문서로 이동")

#### 2. downloadProfileImage(String user_id, HttpServletRequest request)
  - 사용자의 프로필 이미지 가져오기
  - [프로필 이미지 가져오기](https://egluuapi.codingnome.dev//docs/index.html#resourcesProfileImageUpload "해당 API 문서로 이동")
      
      
## 2. Service
### 정의
사용자 프로필 이미지 파일 처리에 대한 비즈니스 로직을 수행

### 공통 레퍼지토리
    - ProfileImageRepository

### 기능 리스트
1. [storeProfileImage(String user_id, MultipartFile file)](#1-storeprofileimagestring-user_id-multipartfile-file) : 프로필 이미지 저장
2. [getProfileImage(String user_id)](#2-getprofileimagestring-user_id) : 프로필 이미지 가져오기

### 기능 정의
#### 1. storeProfileImage(String user_id, MultipartFile file)
  - 인증된 사용자의 프로필 이미지 업로드
  - 정상적인 파일 명 여부 검증
  - 기존 프로필 이미지가 존재할 경우 신규 이미지로 덮어쓰기

  - 연관 예외 클래스
    - FileNameException(fileName)
    - FileUploadException(fileName, exception)

#### 2. getProfileImage(String user_id)
  - 사용자의 프로필 이미지 가져오기

  - 연관 예외 클래스
    - ProfileImageNotFoundException(user_id)
    - FileDownloadException(fileName)
    - FileDownloadException(fileName, exception)
