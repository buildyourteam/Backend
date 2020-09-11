import io.gatling.core.Predef._
import io.gatling.http.Predef._

/*
  [시나리오 개요]
  TestUser3가 기획한 프로젝트 작성 및 생성
*/
class CreateProjectSimulation extends Simulation {
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

  val answers: List[String] = List("1번 응답", "2번 응답", "3번 응답")
  val questions: List[String] = List( "question1", "question2" )

  // 프로젝트 생성
  val createProject = Map (
    "projectName" -> "project0",
    "teamName" -> "project team0",
    "endDate" -> "2020-09-07T13:58:57.155486",
    "introduction" -> "need yes 입니다.",
    "state" -> "RECRUTING",
    "projectField" -> "WEB",
    "applyCanFile" -> true,
    "questions" -> questions,
    "needMember" ->  (
      "developer" -> 0,
      "designer" -> 0,
      "planner" -> 0,
      "etc" -> 0
    )
  )

  val scn = scenario("프로젝트 기획서 작성") // 시나리오 선언
    .exec(http("프로젝트 작성 및 생성") // HTTP 요청 이름
      .post("/projects") // HTTP 메소드 (요청 경로)
      .headers(JWTAuthHeader_TestUser3)
      .formParamMap(createProject)  // 요청 패이로드
      .check(status.is(CREATED)))

  setUp( // 시뮬레이션 지정용 setUp() 메소드
    scn.inject(atOnceUsers(10))
  ).protocols(httpConfiguration) // 위에서 지정한 프로토콜을 사용함
}