# 개요
웹 운영 및 보안에 필요한 구성 설정
1. 트래픽 설정
    - X-Forwarded For HTTP Header 필터 적용
    
2. 로그 설정
    - LogBack 기능 사용
    
3. 보안 설정
    - 사용자 계정 패스워드 암호화 : BCrypt 해싱 함수 사용
    - HTTP Header에 JWT를 삽입하여 사용자 인증 관리
    - CORS(Cross Origin Resource Sharing) 구성
    
# 클래스 구성
## 1. AppConfig
### 정의
웹 애플리케이션 구성 및 보안 유지에 필요한 메소드를 빈으로 등록 및 관리하기 위한 클래스

### 메소드
1. ModelMapper modelMapper()
    - Model Mapper 빈으로 등록

2. FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter()
    - XFF HTTP Hdeader 필터 빈으로 등록

3. FilterRegistrationBean logbackMdcFilterRegistrationBean()
    - Logback을 위한 MDC 필터 빈으로 등록

4. FilterRegistrationBean multiReadableHttpServletRequestFilterRegistrationBean()
    - LogBack 필터가 적용될 수 있도록 FilterRegistration 빈을 등록
  
5. PasswordEncoder passwordEncoder()
    - 사용자 password 암호화 메소드 빈으로 등록

## 2. FileUploadProperties
### 정의
서버에 업로드 되는 파일에 대한 경로를 application 파일에서 가져오기 위한 클래스

## 3. WebMvcConfig
### 정의
사용자 요청에 대한 CORS(Cross Origin Resource Sharing) 구성 설정

### 메소드
1. void addCorsMappings()
    - 지정된 경로 패턴에 대해 CORS 요청 처리를 사용하도록 설정