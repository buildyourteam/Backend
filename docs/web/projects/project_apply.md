# 개요
**프로젝트 지원 인터페이스** 관련하여 아래 항목을 정의
1. 인터페이스 정의
    - 프로젝트 지원 관련 인터페이스에 대한 설명
2. API 문서 링크
    - 각 인터페이스에 대한 API 문서 링크
3. 사용 도메인
    - 인터페이스에서 사용하는 도메인 리스트

# 클래스 구성
## 1. ProjectApplyController
### 정의
웹 서비스 상에서 프로젝트 지원자 관리에 사용되는 인터페이스 제공

### 공통 도메인
- Project
- ProjectApply

### 메소드
1. applyProject()
    - 인증 사용자가 특정 프로젝트에 지원
    - [프로젝트 지원](http://34.105.29.115:8080/docs/index.html#projectApply "해당 API 문서로 이동")
    - 필요한 도메인
        - User
    
2. updateApply()
    - 기존에 지원한 프로젝트에 제출한 지원서 내용을 수정
    - [프로젝트 지원서 수정](http://34.105.29.115:8080/docs/index.html#updateApply "해당 API 문서로 이동")
    - 필요한 도메인
        - ProjectApplyAnswer
            
3. getApplicants()
    - 프로젝트 지원자 리스트를 조회
    - [프로젝트 지원서 수정](http://34.105.29.115:8080/docs/index.html#getApplicants "해당 API 문서로 이동")
    - 필요한 도메인
        - －
                
4. getApply()
    - 특정 유저가 작성한 프로젝트 지원서를 조회
    - [지원서 조회](http://34.105.29.115:8080/docs/index.html#getApply "해당 API 문서로 이동")
    - 필요한 도메인
        - －
        
5. acceptMember()
    - 프로젝트 지원자 중 합류할 인원은 accept
    - [프로젝트 지원자 승인](http://34.105.29.115:8080/docs/index.html#acceptApply "해당 API 문서로 이동")
    - 필요한 도메인
        - ProjectMember
        
6. rejectMember()
    - 프로젝트 지원자 중 거절할 인원 reject
    - [프로젝트 지원자 거절](http://34.105.29.115:8080/docs/index.html#rejectApply "해당 API 문서로 이동")
    - 필요한 도메인
        - Project
        - ProjectApply
   