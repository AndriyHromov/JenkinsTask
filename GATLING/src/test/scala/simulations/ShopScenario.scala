package simulations

import Helpers.BaseHelpers.httpProtocol
import Scenarios.Scenario
import io.gatling.core.Predef._
import scala.concurrent.duration._

class ShopScenario extends Simulation {

	setUp(
		Scenario.mainScenario.inject(

			constantConcurrentUsers(5) during (10.minutes)

		)
	).protocols(httpProtocol)

}