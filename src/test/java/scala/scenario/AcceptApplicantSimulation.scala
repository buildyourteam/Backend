import io.gatling.core.Predef._
import io.gatling.http.Predef._

/*
  [시나리오 개요]
  PK가 1인 프로젝트 리더 (TestUser1)가 TestUser3가 작성한 지원서를 조회 후 승인
*/
class AcceptApplicantSimulation extends Simulation {

  private final var OK = 200
  private final var CREATED = 201

  val JWTAuthHeader_TestUser1 = Map("Authorization" ->
    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUZXN0VXNlcjEiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNTk5NzQxMjU5LCJleHAiOjE2MDAzNDYwNTl9.bXk6NOm_dJWYLtdr4n1H_YiIkxj8ABdppY4l0YIJTjM")

  val JWTAuthHeader_TestUser3 = Map("Authorization" ->
    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUZXN0VXNlcjMiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNTk5NzQxMjc0LCJleHAiOjE2MDAzNDYwNzR9.9FK9OcEBvuFzzN-aP-J55mkH2kUwNDmiDgEA-KFFlRk")

  // 서버에 요청할 HTTP 헤더 설정
  val httpConfiguration = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("*/*")
    .contentTypeHeader("application/json")
    .doNotTrackHeader("1")
    .acceptEncodingHeader("gzip, deflate, br")
    .userAgentHeader("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Mobile Safari/537.36")

  val scn = scenario("프로젝트 지원자 승인") // 시나리오 선언
    .exec(http("프로젝트 상세 정보 조회") // HTTP 요청 이름
      .get("/projects/1") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser1)
      .check(status.is(OK))) // HTTP 메소드 (요청 경로)
    .exec(http("프로젝트 지원서 조회") // HTTP 요청 이름
      .get("/projects/1/apply/TestUser3")
      .headers(JWTAuthHeader_TestUser1)
      .check(status.is(OK))) // HTTP 메소드(요청 경로)
    .pause(30) // 생각하는 시간
    .exec(http("프로젝트 지원서 승인") // HTTP 요청 이름
      .put("/projects/1/apply/TestUser3")
      .headers(JWTAuthHeader_TestUser1)
      .check(status.is(OK))) // HTTP 메소드(요청 경로)

  setUp( // 시뮬레이션 지정용 setUp() 메소드
    scn.inject(atOnceUsers(10))
  ).protocols(httpConfiguration) // 위에서 지정한 프로토콜을 사용함

}