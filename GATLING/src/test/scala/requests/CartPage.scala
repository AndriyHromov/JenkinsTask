package requests

import Helpers.BaseHelpers.{pauseMax, pauseMin}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object CartPage {

  val openCart: ChainBuilder =
    group("Open_Cart_Page") {
      exec(
        http("Open Cart")
          .get("/cart")


          .check(regex("""name=['"]total_net['"]\s*value=['"]([^'"]+)['"]""").saveAs("totalNet"))
          .check(regex("""value=['"]([^'"]+)['"]\s*name=['"]trans_id['"]""").saveAs("transId"))
          .check(regex("""name=['"]cart_content['"]\s*value=['"]([^'"]+)['"]""").optional.saveAs("cartContent"))


      )
        .pause(pauseMin, pauseMax)
    }
  val addToCart: ChainBuilder =
    group("Add_To_Cart") {
      exec(
        http("Add product to cart")
          .post("/wp-admin/admin-ajax.php")
          .formParam("action", "ic_add_to_cart")
          .formParam(
            "add_cart_data",
            "current_product=${productId}&cart_content={%22${productId}__%22:${quantity}}&current_quantity=${quantity}"
          )
          .formParam("cart_container", "0")
          .formParam("cart_widget", "0")
          .check(substring("Added!").exists)
      )
        .pause(pauseMin, pauseMax)
    }

}