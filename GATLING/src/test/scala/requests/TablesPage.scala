package requests

import Helpers.BaseHelpers.{pauseMax, pauseMin}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import scala.util.Random

object TablesPage {
/*
  val openTables: ChainBuilder = {
    group("Open_tables") {
      exec(http("Tables_Category_Page")
        .get("/products-category/tables-2")
        .check(regex("""href="(http[^"]+/products/[^"]+)"""").findAll.saveAs("All_Tables_Urls"))
      )
        .pause(pauseMin, pauseMax)
    }
  }
*/
  val openTable: ChainBuilder = {
    group("Open_Table_Page") {
      exec { session =>
        val urls = session("tables_Products_Urls").asOption[Seq[String]].getOrElse(Nil)

        if (urls.nonEmpty) {
          val selectedUrl = urls(Random.nextInt(urls.size))
          session.set("Random_Table_Url", selectedUrl)
        } else session
      }
        .exec(http("View_Table_Details")
          .get("${Random_Table_Url}")


          .check(regex("""name=['"]current_product['"][\s\S]*?value=['"](\d+)['"]""").saveAs("productId")))
        .pause(pauseMin, pauseMax)
    }
  }


}
