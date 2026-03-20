package Scenarios

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import requests._
import userData.Feeders

object Scenario {

  val mainScenario: ScenarioBuilder =
    scenario("ShopScenario")
  .exec(flushHttpCache)
  .exec(flushCookieJar)
      .exitBlockOnFail {

        feed(Feeders.userFeeder)


        .exec(HomePage.openApp)


          .exec(OpenSection.openCategory("tables", "/products-category/tables-2"))
          .exec(TablesPage.openTable)
          .exec { session =>
            session.set("quantity", scala.util.Random.nextInt(5) + 1)
          }
          .exec(CartPage.addToCart)


          .randomSwitch(
            50.0 -> exec(
              OpenSection.openCategory("chairs", "/chairs")
                .exec(ChairsPage.openChair)
                .exec { session =>
                  session.set("quantity", scala.util.Random.nextInt(5) + 1)
                }
                .exec(CartPage.addToCart)
            )
          )

          .randomSwitch(
            30.0 -> exec(
              CartPage.openCart
                .exec(CheckoutPage.openCheckout)
                .exec(CheckoutPage.loadStates)
                .exec(CheckoutPage.submitOrder)
                .exec(CheckoutPage.openThankYou)
            )
          )
      }
}
