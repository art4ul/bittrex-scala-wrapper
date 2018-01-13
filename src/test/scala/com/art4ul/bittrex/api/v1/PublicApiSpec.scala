package com.art4ul.bittrex.api.v1

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.stream.ActorMaterializer
import akka.testkit.TestKit
import com.art4ul.bittrex.api.MockedRequestSender
import com.art4ul.bittrex.protocol.v1.PublicApiProtocol._
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

  "getMarkets" should "return list of markets " in {
    val api = PublicApiMock(
      """
       {
         |	"success" : true,
         |	"message" : "",
         |	"result" : [{
         |			"MarketCurrency" : "LTC",
         |			"BaseCurrency" : "BTC",
         |			"MarketCurrencyLong" : "Litecoin",
         |			"BaseCurrencyLong" : "Bitcoin",
         |			"MinTradeSize" : 0.01000000,
         |			"MarketName" : "BTC-LTC",
         |			"IsActive" : true,
         |			"Created" : "2014-02-13T00:00:00"
         |		}, {
         |			"MarketCurrency" : "DOGE",
         |			"BaseCurrency" : "BTC",
         |			"MarketCurrencyLong" : "Dogecoin",
         |			"BaseCurrencyLong" : "Bitcoin",
         |			"MinTradeSize" : 100.00000000,
         |			"MarketName" : "BTC-DOGE",
         |			"IsActive" : true,
         |			"Created" : "2014-02-13T00:00:00"
         |		}
         |    ]
         |}
      """.stripMargin)
    val result = Await.result(api.getMarkets, Duration.Inf)
    val expected = List(
      Market(
        `MarketCurrency` = "LTC",
        `BaseCurrency` = "BTC",
        `MarketCurrencyLong` = "Litecoin",
        `BaseCurrencyLong` = "Bitcoin",
        `MinTradeSize` = 0.01000000,
        `MarketName` = "BTC-LTC",
        `IsActive` = true,
        `Created` = "2014-02-13T00:00:00"
      ),
      Market(
        `MarketCurrency` = "DOGE",
        `BaseCurrency` = "BTC",
        `MarketCurrencyLong` = "Dogecoin",
        `BaseCurrencyLong` = "Bitcoin",
        `MinTradeSize` = 100.00000000,
        `MarketName` = "BTC-DOGE",
        `IsActive` = true,
        `Created` = "2014-02-13T00:00:00"
      )

    )
    result shouldBe expected
  }


  "getCurrencies" should "return list of currencies " in {
    val api = PublicApiMock(
      """
       {
         |	"success" : true,
         |	"message" : "",
         |	"result" : [
         | {
         |			"Currency" : "BTC",
         |			"CurrencyLong" : "Bitcoin",
         |			"MinConfirmation" : 2,
         |			"TxFee" : 0.00020000,
         |			"IsActive" : true,
         |			"CoinType" : "BITCOIN",
         |			"BaseAddress" : null
         |		}, {
         |			"Currency" : "LTC",
         |			"CurrencyLong" : "Litecoin",
         |			"MinConfirmation" : 5,
         |			"TxFee" : 0.00200000,
         |			"IsActive" : true,
         |			"CoinType" : "BITCOIN",
         |			"BaseAddress" : null
         |		}
         |    ]
         |}
      """.stripMargin)
    val result = Await.result(api.getCurrencies, Duration.Inf)
    val expected = List(
      Currency(
        `Currency` = "BTC",
        `CurrencyLong` = "Bitcoin",
        `MinConfirmation` = 2,
        `TxFee` = 0.00020000,
        `IsActive` = true,
        `CoinType` = "BITCOIN",
        `BaseAddress` = None
      ),
      Currency(
        `Currency` = "LTC",
        `CurrencyLong` = "Litecoin",
        `MinConfirmation` = 5,
        `TxFee` = 0.00200000,
        `IsActive` = true,
        `CoinType` = "BITCOIN",
        `BaseAddress` = None
      )

    )
    result shouldBe expected
  }

  "getTicker" should "return list of ticks " in {
    val api = PublicApiMock(
      """{
        |	"success" : true,
        |	"message" : "",
        |	"result" : {
        |		"Bid" : 2.05670368,
        |		"Ask" : 3.35579531,
        |		"Last" : 3.35579531
        |	}
        |}
      """.stripMargin)
    val result = Await.result(api.getTicker("BTC-LTC"), Duration.Inf)
    val expected = Ticker(
      `Bid` = 2.05670368,
      `Ask` = 3.35579531,
      `Last` = 3.35579531
    )
    result shouldBe expected
  }

  "getMarketSummaries" should "return list of elements " in {
    val api = PublicApiMock(
      """  {
        |	"success" : true,
        |	"message" : "",
        |	"result" : [{
        |			"MarketName" : "BTC-888",
        |			"High" : 0.00000919,
        |			"Low" : 0.0000082,
        |			"Volume" : 74339.61396015,
        |			"Last" : 0.0000082,
        |			"BaseVolume" : 0.64966963,
        |			"TimeStamp" : "2014-07-09T07:19:30.15",
        |			"Bid" : 0.0000082,
        |			"Ask" : 0.00000831,
        |			"OpenBuyOrders" : 15,
        |			"OpenSellOrders" : 15,
        |			"PrevDay" : 0.00000821,
        |			"Created" : "2014-03-20T06:00:00",
        |			"DisplayMarketName" : null
        |		}, {
        |			"MarketName" : "BTC-A3C",
        |			"High" : 0.00000072,
        |			"Low" : 0.00000001,
        |			"Volume" : 166340678.42280999,
        |			"Last" : 0.00000005,
        |			"BaseVolume" : 17.59720424,
        |			"TimeStamp" : "2014-07-09T07:21:40.51",
        |			"Bid" : 0.00000004,
        |			"Ask" : 0.00000005,
        |			"OpenBuyOrders" : 18,
        |			"OpenSellOrders" : 18,
        |			"PrevDay" : 0.00000002,
        |			"Created" : "2014-05-30T07:57:49.637",
        |			"DisplayMarketName" : null
        |		}
        |    ]
        |}
      """.stripMargin)
    val result = Await.result(api.getMarketSummaries, Duration.Inf)
    val expected = List(
      MarketSummary(
        `MarketName` = "BTC-888",
        `High` = 0.00000919,
        `Low` = 0.0000082,
        `Volume` = 74339.61396015,
        `Last` = 0.0000082,
        `BaseVolume` = 0.64966963,
        `TimeStamp` = "2014-07-09T07:19:30.15",
        `Bid` = 0.0000082,
        `Ask` = 0.00000831,
        `OpenBuyOrders` = 15,
        `OpenSellOrders` = 15,
        `PrevDay` = 0.00000821,
        `Created` = "2014-03-20T06:00:00",
        `DisplayMarketName` = None
      ),
      MarketSummary(
        `MarketName` = "BTC-A3C",
        `High` = 0.00000072,
        `Low` = 0.00000001,
        `Volume` = BigDecimal("166340678.42280999"),
        `Last` = 0.00000005,
        `BaseVolume` = 17.59720424,
        `TimeStamp` = "2014-07-09T07:21:40.51",
        `Bid` = 0.00000004,
        `Ask` = 0.00000005,
        `OpenBuyOrders` = 18,
        `OpenSellOrders` = 18,
        `PrevDay` = 0.00000002,
        `Created` = "2014-05-30T07:57:49.637",
        `DisplayMarketName` = None
      )

    )
    result shouldBe expected
  }


  "getMarketSummary" should "return list of elements " in {
    val api = PublicApiMock(
      """ {
        |	"success" : true,
        |	"message" : "",
        |	"result" : [{
        |			"MarketName" : "BTC-LTC",
        |			"High" : 0.01350000,
        |			"Low" : 0.01200000,
        |			"Volume" : 3833.97619253,
        |			"Last" : 0.01349998,
        |			"BaseVolume" : 47.03987026,
        |			"TimeStamp" : "2014-07-09T07:22:16.72",
        |			"Bid" : 0.01271001,
        |			"Ask" : 0.01291100,
        |			"OpenBuyOrders" : 45,
        |			"OpenSellOrders" : 45,
        |			"PrevDay" : 0.01229501,
        |			"Created" : "2014-02-13T00:00:00",
        |			"DisplayMarketName" : null
        |		}
        |    ]
        |}
      """.stripMargin)
    val result = Await.result(api.getMarketSummary("BTC-LTC"), Duration.Inf)
    val expected =
      List(MarketSummary(
        `MarketName` = "BTC-LTC",
        `High` = 0.01350000,
        `Low` = 0.01200000,
        `Volume` = 3833.97619253,
        `Last` = 0.01349998,
        `BaseVolume` = 47.03987026,
        `TimeStamp` = "2014-07-09T07:22:16.72",
        `Bid` = 0.01271001,
        `Ask` = 0.01291100,
        `OpenBuyOrders` = 45,
        `OpenSellOrders` = 45,
        `PrevDay` = 0.01229501,
        `Created` = "2014-02-13T00:00:00",
        `DisplayMarketName` = None
      ))
    result shouldBe expected
  }


  "getOrderBook" should "return list of elements " in {
    val api = PublicApiMock(
      """{
        |	"success" : true,
        |	"message" : "",
        |	"result" : {
        |		"buy" : [{
        |				"Quantity" : 12.37000000,
        |				"Rate" : 0.02525000
        |			}
        |		],
        |		"sell" : [{
        |				"Quantity" : 32.55412402,
        |				"Rate" : 0.02540000
        |			}, {
        |				"Quantity" : 60.00000000,
        |				"Rate" : 0.02550000
        |			}, {
        |				"Quantity" : 60.00000000,
        |				"Rate" : 0.02575000
        |			}, {
        |				"Quantity" : 84.00000000,
        |				"Rate" : 0.02600000
        |			}
        |		]
        |	}
        |}
      """.stripMargin)
    val result = Await.result(api.getOrderBook("BTC-LTC", `type` = "both"), Duration.Inf)
    val expected =
      OrderBook(
        buy = List(
          ShortOrderDescription(
            `Quantity` = 12.37000000,
            `Rate` = 0.02525000
          )),
        sell = List(
          ShortOrderDescription(
            `Quantity` = 32.55412402,
            `Rate` = 0.02540000
          ), ShortOrderDescription(
            `Quantity` = 60.00000000,
            `Rate` = 0.02550000
          ), ShortOrderDescription(
            `Quantity` = 60.00000000,
            `Rate` = 0.02575000
          ), ShortOrderDescription(
            `Quantity` = 84.00000000,
            `Rate` = 0.02600000
          )
        ))
    result shouldBe expected
  }


  "getMarketHistory" should "return list of elements " in {
    val api = PublicApiMock(
      """{
        |	"success" : true,
        |	"message" : "",
        |	"result" : [{
        |			"Id" : 319435,
        |			"TimeStamp" : "2014-07-09T03:21:20.08",
        |			"Quantity" : 0.30802438,
        |			"Price" : 0.01263400,
        |			"Total" : 0.00389158,
        |			"FillType" : "FILL",
        |			"OrderType" : "BUY"
        |		}, {
        |			"Id" : 319433,
        |			"TimeStamp" : "2014-07-09T03:21:20.08",
        |			"Quantity" : 0.31820814,
        |			"Price" : 0.01262800,
        |			"Total" : 0.00401833,
        |			"FillType" : "PARTIAL_FILL",
        |			"OrderType" : "BUY"
        |		}
        |	]
        |}
      """.stripMargin)
    val result = Await.result(api.getMarketHistory("BTC-LTC"), Duration.Inf)
    val expected = List(
      Order(
        `Id` = 319435,
        `TimeStamp` = "2014-07-09T03:21:20.08",
        `Quantity` = 0.30802438,
        `Price` = 0.01263400,
        `Total` = 0.00389158,
        `FillType` = "FILL",
        `OrderType` = "BUY"
      ),
      Order(
        `Id` = 319433,
        `TimeStamp` = "2014-07-09T03:21:20.08",
        `Quantity` = 0.31820814,
        `Price` = 0.01262800,
        `Total` = 0.00401833,
        `FillType` = "PARTIAL_FILL",
        `OrderType` = "BUY"
      )
    )
    result shouldBe expected
  }
}
