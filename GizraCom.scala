import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class GizraCom extends Simulation {
  val testServerUrl = scala.util.Properties.envOrElse("GIZRA_COM_BASE_URL", "https://www.gizra.com")

  val httpProtocol = http
    .baseURL(testServerUrl)
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .doNotTrackHeader("1")
    .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64; rv:58.0) Gecko/20100101 Firefox/58.0")

  val scn = scenario("Gizra")
    .exec(http("request_0")
      .get("/blog")
      .check(css("#blog-page .content:first-of-type a", "href").saveAs("blogentry"))
    )
    .exec(http("request_1")
      .get("${blogentry}")
    )

  setUp(scn.inject(
    rampUsers(10) over(10 seconds),
  )).protocols(httpProtocol)

}
