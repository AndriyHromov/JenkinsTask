package requests

import Helpers.BaseHelpers.{pauseMax, pauseMin}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object CheckoutPage {

  val openCheckout: ChainBuilder =
    group("Open_Checkout_Page") {
      exec(
        http("Open Checkout")
          .post("/checkout")
          .formParam("shipping", "order")
      )
        .pause(pauseMin, pauseMax)
    }


  val loadStates: ChainBuilder =
    group("Load_States") {
      exec(
        http("Load States")
          .post("/wp-admin/admin-ajax.php")
          .header("Content-Type", "application/x-www-form-urlencoded")
          .formParam("action", "ic_state_dropdown")
          .formParam("country_code", "UA")
          .formParam("state_code", "")


      )
        .pause(pauseMin, pauseMax)
    }
  val submitOrder: ChainBuilder =
    group("Submit_order") {
      doIf(session => session.contains("cartContent")) {
        exec(
          http("Submit Order")
            .post("/checkout")
            .formParam("ic_formbuilder_redirect", "http://localhost/thank-you")


            .formParam("cart_content", "${cartContent}")
            .formParam("total_net", "${totalNet}")
            .formParam("trans_id", "${transId}")
            .formParam("shipping", "order")
            .formParam("cart_type", "order")




            .formParam("cart_company", "")
            .formParam("cart_name", "${name}")
            .formParam("cart_address", "${address}")
            .formParam("cart_postal", "${postal}")
            .formParam("cart_city", "${city}")
            .formParam("cart_country", "UA")
            .formParam("cart_state", "")
            .formParam("cart_phone", "${phone}")
            .formParam("cart_email", "${email}")
            .formParam("cart_comment", "")


            // submit
            .formParam("cart_submit", "Place Order")

        )
          .pause(pauseMin, pauseMax)
      }
    }
  val openThankYou: ChainBuilder =
    group("Open_Thank_You") {
      exec(
        http("Open Thank You")
          .get("/thank-you")
      )
    }

}