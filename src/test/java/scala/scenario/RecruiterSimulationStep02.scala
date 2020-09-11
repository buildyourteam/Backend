import io.gatling.core.Predef._
import io.gatling.http.Predef._

/*
  [시나리오 개요]
  TestUser1가 구인자 목록 (3 page)과 구인자 프로필 (5개) 조회 후 TestUser10 에게 영입 제안
*/
class RecruiterSimulationStep02 extends Simulation {

  private final var OK = 200
  private final var CREATED = 201

  val JWTAuthHeader_TestUser1 = Map("Authorization" ->
    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUZXN0VXNlcjEiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNTk5NzQyMDczLCJleHAiOjE2MDAzNDY4NzN9.fUDbdA3HcOn7axZHw9JsRNy2AVxbmuBr7ePFUmJxV9c")

  // 서버에 요청할 HTTP 헤더 설정
  val httpConfiguration = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/hal+json, text/plain, text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .contentTypeHeader("application/json;charset=UTF-8")
    .acceptEncodingHeader("gzip, deflate, br")
    .userAgentHeader("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Mobile Safari/537.36")

  val requestHeader = Map(
    "origin" -> "",
    "Content-Type" -> "application/json;charset=UTF-8",
    "Accept" -> "application/hal+json",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Authorization" -> "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUZXN0VXNlcjEiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNTk5NzQyMDczLCJleHAiOjE2MDAzNDY4NzN9.fUDbdA3HcOn7axZHw9JsRNy2AVxbmuBr7ePFUmJxV9c"
  )

  val scn = scenario("리쿠르터") // 시나리오 선언
    .exec(http("영입 제안서 작성 및 제출") // HTTP 요청 이름
      .post("/profile/TestUser10/recruit") // HTTP 메소드(요청 경로)
      .headers(requestHeader)
      .body(StringBody("""
                    {
                      "introduction" : "플젝에 영입하고 싶어요",
                      "role" : "DEVELOPER",
                      "projectId" : 1
                    }"""
      )).asJson
      .check(status.is(CREATED)))
    .exec(http("People 리스트 조회 - 1 page") // HTTP 요청 이름
      .get("/people?page=0&size=3") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser1)
      .check(status.is(OK)))
    .pause(5) // 생각하는 시간
    .exec(http("People 리스트 조회 - 2 page") // HTTP 요청 이름
      .get("/people?page=1&size=3") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser1)
      .check(status.is(OK)))
    .pause(5) // 생각하는 시간
    .exec(http("People 리스트 조회 - 3 page") // HTTP 요청 이름
      .get("/people?page=2&size=3") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser1)
      .check(status.is(OK)))
    .pause(5) // 생각하는 시간
    .exec(http("사용자 프로필 조회 - TestUser6") // HTTP 요청 이름
      .get("/profile/TestUser6") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser1)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간
    .exec(http("사용자 프로필 조회 - TestUser7") // HTTP 요청 이름
      .get("/profile/TestUser7") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser1)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간
    .exec(http("사용자 프로필 조회 - TestUser8") // HTTP 요청 이름
      .get("/profile/TestUser8") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser1)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간
    .exec(http("사용자 프로필 조회 - TestUser9") // HTTP 요청 이름
      .get("/profile/TestUser9") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser1)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간
    .exec(http("사용자 프로필 조회 - TestUser10") // HTTP 요청 이름
      .get("/profile/TestUser10") // HTTP 메소드(요청 경로)
      .headers(JWTAuthHeader_TestUser1)
      .check(status.is(OK)))
    .pause(10) // 생각하는 시간
//    .exec(http("영입 제안서 작성 및 제출") // HTTP 요청 이름
//      .post("/profile/TestUser10/recruit") // HTTP 메소드(요청 경로)
//      .headers(JWTAuthHeader_TestUser1)
//      .formParamMap(recruitProject)
//      .check(status.is(CREATED)))
    .pause(30) // 생각하는 시간

  setUp( // 시뮬레이션 지정용 setUp() 메소드
    scn.inject(atOnceUsers(10))
  ).protocols(httpConfiguration) // 위에서 지정한 프로토콜을 사용함
}