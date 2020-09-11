import io.gatling.core.Predef._
import io.gatling.http.Predef._

/*
  [시나리오 개요]
  TestUser3가 PK가 1인 Project 에 지원
*/
class ApplyProjectSimulation extends Simulation {

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

  // 프로젝트 지원
  val applyProject = Map (
    "answers" -> "hello",
    "introduction" -> "안녕하세요? 저는 그냥 개발자입니다.",
    "role" -> "DEVELOPER"
  )

  val scn = scenario("프로젝트 지원 희망자") // 시나리오 선언
    .exec(http("사용자 프로필 조회") // HTTP 요청 이름
      .get("/profile/TestUser1")
      .headers(JWTAuthHeader_TestUser3) // HTTP 메소드 (요청 경로)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간
    .exec(http("프로젝트 리스트 조회") // HTTP 요청 이름
      .get("/projects?page=0&size=8")
      .headers(JWTAuthHeader_TestUser3) // HTTP 메소드(요청 경로)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간
    .exec(http("특정 프로젝트 지원을 위한 지원서 작성 및 제출") // HTTP 요청 이름
      .post("/projects/1/apply")
      .headers(JWTAuthHeader_TestUser3)
      .formParamMap(applyProject)
      .check(status.is(CREATED))) // HTTP 메소드 (요청 경로)
    .pause(60) // 생각하는 시간

  setUp( // 시뮬레이션 지정용 setUp() 메소드
    scn.inject(atOnceUsers(10))
  ).protocols(httpConfiguration) // 위에서 지정한 프로토콜을 사용함
}
