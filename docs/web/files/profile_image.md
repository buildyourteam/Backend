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
1. `POST` `/profile/image/{user_id}` [uploadProfileImage()](#1-uploadprofileimage) : 프로필 사진 업로드
2. `GET` `/profile/image/{user_id}` [downloadProfileImage()](#2-downloadprofileimage) : 프로필 사진 가져오기

### 기능 정의
#### 1. uploadProfileImage()
  - 인증된 사용자의 프로필 이미지 업로드
  - [프로필 이미지 업로드](https://egluuapi.codingnome.dev//docs/index.html#resourcesProfileImageUpload "해당 API 문서로 이동")
  - 연관 엔터티
      - －

#### 2. downloadProfileImage()
  - 인증된 사용자의 프로필 이미지 가져오기
  - [프로필 이미지 가져오기](https://egluuapi.codingnome.dev//docs/index.html#resourcesProfileImageUpload "해당 API 문서로 이동")
  - 연관 엔터티
      - －
      
## 2. Service
### 정의
쭉 하기