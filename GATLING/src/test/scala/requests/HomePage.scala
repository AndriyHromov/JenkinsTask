package requests

import Helpers.BaseHelpers.{pauseMax, pauseMin}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object HomePage {

  val openApp: ChainBuilder =
    group("Open_Home_Page") {
      exec(
        http("Open Home Page")
          .get("/")

      )
        .pause(pauseMin, pauseMax)
    }
}