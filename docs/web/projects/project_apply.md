# 개요
**프로젝트 지원 인터페이스** 관련하여 아래 항목을 정의
1. 인터페이스 정의
    - 프로젝트 지원 관련 인터페이스에 대한 설명
2. API 문서 링크
    - 각 인터페이스에 대한 API 문서 링크
3. 연관 엔터티
    - 인터페이스에서 연관되는 엔터티 리스트

# 클래스 구성
## 1. Controller
### 정의
웹 서비스 상에서 프로젝트 지원자 관리에 사용되는 인터페이스 제공

### 공통 엔터티
- Project
- ProjectApply

### 기능 리스트
1. `POST` `/projects/{projectId}/apply` [applyProject()](####1.-applyproject()) : 프로젝트 지원
2. `PUT` `/projects/{projectId}/apply` [updateApply()](####2.-updateapply()) : 프로젝트 수정
3. `GET` `/projects/{projectId}/apply` [getApplicants()](####3.-getapplicants()) : 지원자 리스트 조회
4. `GET` `/projects/{projectId}/apply/{userId}` [getApply()](####4.-getapply()) : 프로젝트 지원서 조회
5. `PUT` `/projects/{projectId}/apply/{userId}`[acceptMember()](####5.-acceptmember()) : 프로젝트 참여 승인
6. `DELETE` `/projects/{projectId}/apply/{userId}` [rejectMember()](####6.-rejectmember()) : 프로젝트 참여 거절

### 기능 정의
#### 1. applyProject()  
  - 인증 사용자가 특정 프로젝트에 지원
  - [프로젝트 지원](http://34.105.29.115:8080/docs/index.html#projectApply "해당 API 문서로 이동")
  - 연관 엔터티
      - User
    
#### 2. updateApply()
  - 프로젝트 지원자가 기존에 지원한 프로젝트에 제출한 지원서 내용을 수정
  - [프로젝트 지원서 수정](http://34.105.29.115:8080/docs/index.html#updateApply "해당 API 문서로 이동")  
    - 연관 엔터티
      - ProjectApplyAnswer
            
#### 3. getApplicants()
  - 프로젝트 리더가 프로젝트에 지원한 인원에 대한 리스트를 조회
  - [프로젝트 지원서 수정](http://34.105.29.115:8080/docs/index.html#getApplicants "해당 API 문서로 이동")
  - 연관 엔터티
        - －
                
#### 4. getApply()
  - 프로젝트 리더가 특정 유저가 작성한 프로젝트 지원서를 조회
  - [지원서 조회](http://34.105.29.115:8080/docs/index.html#getApply "해당 API 문서로 이동")
  - 연관 엔터티
      - －
        
#### 5. acceptMember()
  - 프로젝트 리더가 프로젝트 지원자 중 합류할 인원을 accept
  - [프로젝트 지원자 승인](http://34.105.29.115:8080/docs/index.html#acceptApply "해당 API 문서로 이동")
  - 연관 엔터티
      - ProjectMember
        
#### 6. rejectMember()
  - 프로젝트 리더가 프로젝트 지원자 중 거절할 인원 reject
  - [프로젝트 지원자 거절](http://34.105.29.115:8080/docs/index.html#rejectApply "해당 API 문서로 이동")
  - 연관 엔터티
      - －

## 2. Service
### 정의
쭉 하기