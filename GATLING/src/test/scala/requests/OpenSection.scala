package requests
import Helpers.BaseHelpers.{pauseMax, pauseMin}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import scala.util.Random

object OpenSection {


 def openCategory(categoryName: String, categoryUrl: String): ChainBuilder =
      group(s"Open_$categoryName") {
        exec(
          http(s"${categoryName}_Category_Page")
            .get(categoryUrl)
            .check(
              regex("""href="(http[^"]+/products/[^"]+)"""")
                .findAll
                .saveAs(s"${categoryName}_Products_Urls")
            )
        )
          .pause(pauseMin, pauseMax)
      }
}