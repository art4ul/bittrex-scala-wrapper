package com.art4ul.bittrex.api.v2

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.stream.ActorMaterializer
import akka.testkit.TestKit
import com.art4ul.bittrex.api.MockedRequestSender
import com.art4ul.bittrex.protocol.v2.PublicApiProtocol._
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}
import com.art4ul.bittrex.util.DateUtil._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class PublicApiSpec extends TestKit(ActorSystem("PublicApiSpec"))
  with FlatSpecLike
  with Matchers
  with BeforeAndAfterAll {

  implicit val execContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  class PublicApiMock(val mockedResponse: HttpResponse) extends PublicApi() with MockedRequestSender

  object PublicApiMock {
    def apply(body: String): PublicApiMock = new PublicApiMock(HttpResponse(
      entity = HttpEntity(string = body, contentType = ContentTypes.`application/json`))
    )
  }


  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "getTicks" should "return list of Ticks " in {
    val api = PublicApiMock(
      """
        {"success":true,"message":"","result":[
          {"O":0.01878661,"H":0.01891502,"L":0.01874331,"C":0.01887067,"V":702.51047321,"T":"2017-12-22T23:00:00","BV":13.21569416},
          {"O":0.01890000,"H":0.01898999,"L":0.01868665,"C":0.01875003,"V":1038.10592542,"T":"2017-12-22T23:05:00","BV":19.53353420}
        ]}
      """.stripMargin)
    val result = Await.result(api.getTicks("BTC-LTC", TickInterval.Day), Duration.Inf)
    val expected = List(
      Tick(`O` = 0.01878661, `H` = 0.01891502, `L` = 0.01874331, `C` = 0.01887067, `V` = 702.51047321, `T` = "2017-12-22T23:00:00", `BV` = 13.21569416),
      Tick(`O` = 0.01890000, `H` = 0.01898999, `L` = 0.01868665, `C` = 0.01875003, `V` = 1038.10592542, `T` = "2017-12-22T23:05:00", `BV` = 19.53353420)
    )
    result shouldBe expected
  }

  "getLatestTick" should "return last tick " in {
    val api = PublicApiMock(
      """
        {"success":true,"message":"","result":[
          {"O":0.01878661,"H":0.01891502,"L":0.01874331,"C":0.01887067,"V":702.51047321,"T":"2017-12-22T23:00:00","BV":13.21569416}
        ]}
      """.stripMargin)
    val result = Await.result(api.getLatestTick("BTC-LTC", TickInterval.Day), Duration.Inf)
    val expected = List(
      Tick(`O` = 0.01878661, `H` = 0.01891502, `L` = 0.01874331, `C` = 0.01887067, `V` = 702.51047321, `T` = "2017-12-22T23:00:00", `BV` = 13.21569416)
    )
    result shouldBe expected
  }

  "getMarketSummary" should "return summary about market" in {
    val api = PublicApiMock(
      """|{
         |    "message" : "",
         |    "success" : true,
         |    "result" : {
         |        "MarketName": "BTC-ETH",
         |        "High": 0.07817998,
         |        "Low": 0.06604577,
         |        "Volume": 45628.13656093,
         |        "Last": 0.07067988,
         |        "BaseVolume": 3289.09283520,
         |        "TimeStamp": "2017-07-28T20:28:27.797",
         |        "Bid": 0.07067989,
         |        "Ask": 0.07089528,
         |        "OpenBuyOrders": 2743,
         |        "OpenSellOrders": 14066,
         |        "PrevDay": 0.07800000,
         |        "Created": "2015-08-14T09:02:24.817"
         |    }
         |}""".stripMargin)
    val result: MarketSummary = Await.result(api.getMarketSummary("BTC-ETH"), Duration.Inf)
    val expected = MarketSummary(
      `MarketName` = "BTC-ETH",
      `High` = 0.07817998,
      `Low` = 0.06604577,
      `Volume` = 45628.13656093,
      `Last` = 0.07067988,
      `BaseVolume` = 3289.09283520,
      `TimeStamp` = "2017-07-28T20:28:27.797",
      `Bid` = 0.07067989,
      `Ask` = 0.07089528,
      `OpenBuyOrders` = 2743,
      `OpenSellOrders` = 14066,
      `PrevDay` = 0.07800000,
      `Created` = "2015-08-14T09:02:24.817"
    )
    result shouldBe expected
  }


  "getMarketSummaries" should "return summary about all markets" in {
    val api = PublicApiMock(
      """|{
         |    "message" : "",
         |    "success" : true,
         |    "result" : [{
         |        "IsVerified" : false,
         |        "Market" : {
         |            "BaseCurrency" : "BITCNY",
         |            "BaseCurrencyLong" : "BitCNY",
         |            "Created": "2015-12-11T06:31:40.653",
         |            "IsActive" : true,
         |            "IsSponsored" : null,
         |            "LogoUrl" : null,
         |            "MarketCurrency" : "BTC",
         |            "MarketCurrencyLong" : "Bitcoin",
         |            "MarketName" : "BITCNY-BTC",
         |            "MinTradeSize" : 1e-8,
         |            "Notice" : ""
         |        },
         |        "Summary" : {
         |            "MarketName": "BTC-ETH",
         |            "High": 0.07817998,
         |            "Low": 0.06604577,
         |            "Volume": 45628.13656093,
         |            "Last": 0.07067988,
         |            "BaseVolume": 3289.09283520,
         |            "TimeStamp": "2017-07-28T20:28:27.797",
         |            "Bid": 0.07067989,
         |            "Ask": 0.07089528,
         |            "OpenBuyOrders": 2743,
         |            "OpenSellOrders": 14066,
         |            "PrevDay": 0.07800000,
         |            "Created": "2015-08-14T09:02:24.817"
         |        }
         |    }]
         |}""".stripMargin)
    val result: List[GetMarketSummariesItem] = Await.result(api.getMarketSummaries, Duration.Inf)
    val expected = List(
      GetMarketSummariesItem(
        `IsVerified` = false,
        `Summary` = MarketSummary(
          `MarketName` = "BTC-ETH",
          `High` = 0.07817998,
          `Low` = 0.06604577,
          `Volume` = 45628.13656093,
          `Last` = 0.07067988,
          `BaseVolume` = 3289.09283520,
          `TimeStamp` = "2017-07-28T20:28:27.797",
          `Bid` = 0.07067989,
          `Ask` = 0.07089528,
          `OpenBuyOrders` = 2743,
          `OpenSellOrders` = 14066,
          `PrevDay` = 0.07800000,
          `Created` = "2015-08-14T09:02:24.817"
        ),
        `Market` = Market(
          `BaseCurrency` = "BITCNY",
          `BaseCurrencyLong` = "BitCNY",
          `Created` = "2015-12-11T06:31:40.653",
          `IsActive` = true,
          `IsSponsored` = None,
          `LogoUrl` = None,
          `MarketCurrency` = "BTC",
          `MarketCurrencyLong` = Some("Bitcoin"),
          `MarketName` = Some("BITCNY-BTC"),
          `MinTradeSize` = 1e-8,
          `Notice` = Some("")
        )
      ))
    result shouldBe expected
  }
}


