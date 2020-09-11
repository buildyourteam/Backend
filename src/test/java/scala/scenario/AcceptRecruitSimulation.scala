import io.gatling.core.Predef._
import io.gatling.http.Predef._

/*
  [시나리오 개요]
  TestUser1에게 영입 제안받은 TestUser3가 해당 프로젝트 리더 프로필과 상세 정보 조회 후 수락
*/
class AcceptRecruitSimulation extends Simulation {


  private final var OK = 200
  private final var CREATED = 201

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

  val scn = scenario("프로젝트 영입 제안 수락") // 시나리오 선언
    .exec(http("영입 제안을 받은 프로젝트 상세 정보 조회") // HTTP 요청 이름
      .get("/projects/1") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK)))
    .exec(http("영입 제안 리스트 조회") // HTTP 요청 이름
      .get("/profile/TestUser1/recruit") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK)))
    .pause(5) // 생각하는 시간
    .exec(http("영입 상세 정보 조회") // HTTP 요청 이름
      .get("/profile/TestUser1/recruit/1") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간
    .exec(http("영입 제안을 받은 프로젝트 상세 정보 조회") // HTTP 요청 이름
      .get("/projects/1") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK)))
    .pause(20) // 생각하는 시간
    .exec(http("사용자 프로필 조회") // HTTP 요청 이름
      .get("/profile/TestUser1") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간
    .exec(http("프로젝트 조회") // HTTP 요청 이름
      .get("/projects/1") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간
    .exec(http("영입 제안 승인") // HTTP 요청 이름
      .put("/profile/TestUser10/recruit/1") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK)))

  setUp( // 시뮬레이션 지정용 setUp() 메소드
    scn.inject(atOnceUsers(10))
  ).protocols(httpConfiguration) // 위에서 지정한 프로토콜을 사용함
}