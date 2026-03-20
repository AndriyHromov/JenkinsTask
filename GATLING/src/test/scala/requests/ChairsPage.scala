package requests

import Helpers.BaseHelpers.{pauseMax, pauseMin}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import scala.util.Random

object ChairsPage {

 /* val openChairs: ChainBuilder =
    group("Open_chairs") {
      exec(http("Navigate_Chairs_Page")
        .get("/chairs")

        .check(regex("""href="(http[^"]+/products/[^"]+)"""").findAll.saveAs("All_Chairs_Urls"))

      )
        .pause(pauseMin, pauseMax)
    }
    */

  val openChair: ChainBuilder =
    group("Open_Chair_Page") {
      exec { session =>
        val urls = session("chairs_Products_Urls").asOption[Seq[String]].getOrElse(Nil)

        if (urls.nonEmpty) {
          val selectedUrl = urls(Random.nextInt(urls.size))
          session.set("Random_Chair_Url", selectedUrl)
        } else session
      }
        .exec(http("View_Chair_Details")
          .get("${Random_Chair_Url}")


          .check(regex("""name=['"]current_product['"][\s\S]*?value=['"](\d+)['"]""").saveAs("productId")))
        .pause(pauseMin, pauseMax)
    }
}