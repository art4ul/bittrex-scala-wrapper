package com.art4ul.bittrex.api.v1

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.stream.ActorMaterializer
import akka.testkit.TestKit
import com.art4ul.bittrex.api.MockedRequestSender
import com.art4ul.bittrex.protocol.v1.MarketApiProtocol._
import com.art4ul.bittrex.protocol._
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}
import com.art4ul.bittrex.util.DateUtil._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class MarketApiSpec extends TestKit(ActorSystem("MarketApiSpec"))
  with FlatSpecLike
  with Matchers
  with BeforeAndAfterAll {

  implicit val materializer = ActorMaterializer()

  class MarketApiMock(val mockedResponse: HttpResponse) extends MarketApi("", "") with MockedRequestSender

  object MarketApiMock {
    def apply(body: String): MarketApiMock = new MarketApiMock(HttpResponse(
      entity = HttpEntity(string = body, contentType = ContentTypes.`application/json`))
    )
  }


  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "buylimit" should "return list of markets " in {
    val api = MarketApiMock(
      """
       {
       |	"success" : true,
       |	"message" : "",
       |	"result" : {
       |			"uuid" : "e606d53c-8d70-11e3-94b5-425861b86ab6"
       |		}
       |}
      """.stripMargin)
    val result = Await.result(api.buyLimit("BTC-LTC", BigDecimal(1, 0), BigDecimal(2.0)), Duration.Inf)
    val expected = UuidResponse("e606d53c-8d70-11e3-94b5-425861b86ab6")
    result shouldBe expected
  }


  "selllimit" should "return list of markets " in {
    val api = MarketApiMock(
      """
       {
       |	"success" : true,
       |	"message" : "",
       |	"result" : {
       |			"uuid" : "614c34e4-8d71-11e3-94b5-425861b86ab6"
       |		}
       |}
      """.stripMargin)
    val result = Await.result(api.sellLimit("BTC-LTC", BigDecimal(1, 0), BigDecimal(2.0)), Duration.Inf)
    val expected = UuidResponse("614c34e4-8d71-11e3-94b5-425861b86ab6")
    result shouldBe expected
  }


  "getopenorders" should "return list of markets " in {
    val api = MarketApiMock(
      """
       {
       |	"success" : true,
       |	"message" : "",
       |	"result" : [{
       |			"Uuid" : null,
       |			"OrderUuid" : "09aa5bb6-8232-41aa-9b78-a5a1093e0211",
       |			"Exchange" : "BTC-LTC",
       |			"OrderType" : "LIMIT_SELL",
       |			"Quantity" : 5.00000000,
       |			"QuantityRemaining" : 5.00000000,
       |			"Limit" : 2.00000000,
       |			"CommissionPaid" : 0.00000000,
       |			"Price" : 0.00000000,
       |			"PricePerUnit" : null,
       |			"Opened" : "2014-07-09T03:55:48.77",
       |			"Closed" : null,
       |			"CancelInitiated" : false,
       |			"ImmediateOrCancel" : false,
       |			"IsConditional" : false,
       |			"Condition" : null,
       |			"ConditionTarget" : null
       |		}, {
       |			"Uuid" : null,
       |			"OrderUuid" : "8925d746-bc9f-4684-b1aa-e507467aaa99",
       |			"Exchange" : "BTC-LTC",
       |			"OrderType" : "LIMIT_BUY",
       |			"Quantity" : 100000.00000000,
       |			"QuantityRemaining" : 100000.00000000,
       |			"Limit" : 0.00000001,
       |			"CommissionPaid" : 0.00000000,
       |			"Price" : 0.00000000,
       |			"PricePerUnit" : null,
       |			"Opened" : "2014-07-09T03:55:48.583",
       |			"Closed" : null,
       |			"CancelInitiated" : false,
       |			"ImmediateOrCancel" : false,
       |			"IsConditional" : false,
       |			"Condition" : null,
       |			"ConditionTarget" : null
       |		}
       |	]
       |}
       |
      """.stripMargin)
    val result = Await.result(api.getOpenOrders("BTC-LTC"), Duration.Inf)
    val expected = List(
      OpenOrder(`Uuid` = None,
        `OrderUuid` = "09aa5bb6-8232-41aa-9b78-a5a1093e0211",
        `Exchange` = "BTC-LTC",
        `OrderType` = "LIMIT_SELL",
        `Quantity` = 5.00000000,
        `QuantityRemaining` = 5.00000000,
        `Limit` = 2.00000000,
        `CommissionPaid` = 0.00000000,
        `Price` = 0.00000000,
        `PricePerUnit` = None,
        `Opened` = "2014-07-09T03:55:48.77",
        `Closed` = None,
        `CancelInitiated` = false,
        `ImmediateOrCancel` = false,
        `IsConditional` = false,
        `Condition` = None,
        `ConditionTarget` = None
      ),
      OpenOrder(
        `Uuid` = None,
        `OrderUuid` = "8925d746-bc9f-4684-b1aa-e507467aaa99",
        `Exchange` = "BTC-LTC",
        `OrderType` = "LIMIT_BUY",
        `Quantity` = 100000.00000000,
        `QuantityRemaining` = 100000.00000000,
        `Limit` = 0.00000001,
        `CommissionPaid` = 0.00000000,
        `Price` = 0.00000000,
        `PricePerUnit` = None,
        `Opened` = "2014-07-09T03:55:48.583",
        `Closed` = None,
        `CancelInitiated` = false,
        `ImmediateOrCancel` = false,
        `IsConditional` = false,
        `Condition` = None,
        `ConditionTarget` = None
      )

    )
    result shouldBe expected
  }


}
