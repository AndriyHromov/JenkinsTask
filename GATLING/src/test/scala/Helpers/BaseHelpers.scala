package Helpers

import io.gatling.core.Predef.{BlackList, WhiteList, configuration}
import io.gatling.http.Predef.http
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.core.Predef._


object BaseHelpers {
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://wp")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")

  val pauseMin = 2
  val pauseMax = 5
}
