
import io.gatling.core.Predef._
import io.gatling.http.Predef._

/*
  [시나리오 개요]
  TestUser3가 프로젝트 리스트 (3개 페이지)와 특정 프로젝트에 대한 상세 정보 (5개) 조회
 */
class ViewProjectSimulationStep01 extends Simulation {

  private final var OK = 200
  private final var CREATED = 201

  val JWTAuthHeader_TestUser3 = Map("Authorization" ->
    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUZXN0VXNlcjMiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNTk5NzQxMjc0LCJleHAiOjE2MDAzNDYwNzR9.9FK9OcEBvuFzzN-aP-J55mkH2kUwNDmiDgEA-KFFlRk")

  // 서버에 요청할 HTTP 헤더 설정
  val httpConfiguration = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("*/*")
    .doNotTrackHeader("1")
    .acceptEncodingHeader("gzip, deflate, br")
    .userAgentHeader("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Mobile Safari/537.36")

  val scn = scenario("프로젝트 구경하는 사람") // 시나리오 선언
    .exec(http("프로젝트 리스트 3개 조회 - 1 page") // HTTP 요청 이름
      .get("/projects?page=0&size=8") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK))
    )
    .pause(5) // 생각하는 시간
    .exec(http("프로젝트 리스트 3개 조회 - 2 page") // HTTP 요청 이름
      .get("/projects?page=1&size=8") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK))
    )
    .pause(5) // 생각하는 시간
    .exec(http("프로젝트 리스트 3개 조회 - 1 page") // HTTP 요청 이름
      .get("/projects?page=0&size=8") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK)))
    .pause(5) // 생각하는 시간
    .exec(http("프로젝트 상세 정보 5개 가량 조회") // HTTP 요청 이름
      .get("/projects/1") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간
    .exec(http("프로젝트 상세 정보 조회 - 2회") // HTTP 요청 이름
      .get("/projects/1") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간
    .exec(http("프로젝트 상세 정보 조회 - 3회") // HTTP 요청 이름
      .get("/projects/3") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간
    .exec(http("프로젝트 상세 정보 조회 - 4회") // HTTP 요청 이름
      .get("/projects/4") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간
    .exec(http("프로젝트 상세 정보 조회 - 5회") // HTTP 요청 이름
      .get("/projects/5") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간

  setUp( // 시뮬레이션 지정용 setUp() 메소드
    scn.inject(atOnceUsers(10))
  ).protocols(httpConfiguration) // 위에서 지정한 프로토콜을 사용함

}