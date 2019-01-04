package com.art4ul.bittrex.protocol.v2

import java.time.LocalDateTime

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.art4ul.bittrex.protocol._
import spray.json.DefaultJsonProtocol

object PublicApiProtocol extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val tickFormat = jsonFormat7(Tick)
  implicit val marketFormat = jsonFormat11(Market)
  implicit val marketSummaryFormat = jsonFormat13(MarketSummary)
  implicit val marketSummaryResponseFormat = jsonFormat3(GetMarketSummariesItem)
  implicit val marketHistoryFormat = jsonFormat7(MarketHistory)

  /*
     {
        BV: 13.14752793,          // base volume
        C: 0.000121,              // close
        H: 0.00182154,            // high
        L: 0.0001009,             // low
        O: 0.00182154,            // open
        T: "2017-07-16T23:00:00", // timestamp
        V: 68949.3719684          // 24h volume
    }
    */
  case class Tick(`BV`: BigDecimal,
                  `C`: BigDecimal,
                  `H`: BigDecimal,
                  `L`: BigDecimal,
                  `O`: BigDecimal,
                  `T`: LocalDateTime,
                  `V`: BigDecimal)


  object TickInterval extends Enumeration {
    type TickInterval = Value
    val OneMin = Value("oneMin")
    val FiveMin = Value("fiveMin")
    val ThirtyMin = Value("thirtyMin")
    val Hour = Value("hour")
    val Day = Value("day")
  }

  /*
      {
            BaseCurrency : "BITCNY",
            BaseCurrencyLong : "BitCNY",
            Created : "2015-12-11T06:31:40.653",
            IsActive : true,
            IsSponsored : null,
            LogoUrl : null,
            MarketCurrency : "BTC",
            MarketCurrencyLong : "Bitcoin",
            MarketName : "BITCNY-BTC",
            MinTradeSize : 1e-8,
            Notice : ""
        }
   */
  case class Market(`BaseCurrency`: String,
                    `BaseCurrencyLong`: String,
                    `Created`: LocalDateTime,
                    `IsActive`: Boolean,
                    `IsSponsored`: Option[Boolean],
                    `LogoUrl`: Option[String],
                    `MarketCurrency`: String,
                    `MarketCurrencyLong`: Option[String],
                    `MarketName`: Option[String],
                    `MinTradeSize`: BigDecimal,
                    `Notice`: Option[String])


  /*
    {
        MarketName: "BTC-ETH",
        High: 0.07817998,
        Low: 0.06604577,
        Volume: 45628.13656093,
        Last: 0.07067988,
        BaseVolume: 3289.09283520,
        TimeStamp: "2017-07-28T20:28:27.797",
        Bid: 0.07067989,
        Ask: 0.07089528,
        OpenBuyOrders: 2743,
        OpenSellOrders: 14066,
        PrevDay: 0.07800000,
        Created: "2015-08-14T09:02:24.817"
    }
   */
  case class MarketSummary(`MarketName`: String,
                           `High`: BigDecimal,
                           `Low`: BigDecimal,
                           `Volume`: BigDecimal,
                           `Last`: BigDecimal,
                           `BaseVolume`: BigDecimal,
                           `TimeStamp`: LocalDateTime,
                           `Bid`: BigDecimal,
                           `Ask`: BigDecimal,
                           `OpenBuyOrders`: Long,
                           `OpenSellOrders`: Long,
                           `PrevDay`: BigDecimal,
                           `Created`: LocalDateTime)

  /*

   */
  case class GetMarketSummariesItem(`IsVerified`: Boolean,
                                    `Market`: Market,
                                    `Summary`: MarketSummary)


  /*
{
        Id : 106193509,
        TimeStamp : "2017-08-29T18:54:42.613",
        Quantity : 0.09456326,
        Price : 0.08099999,
        Total : 0.00765962,
        FillType : "FILL",
        OrderType : "BUY"
    }
   */
  case class MarketHistory(`Id`: Long,
                           `TimeStamp`: LocalDateTime,
                           `Quantity`: BigDecimal,
                           `Price`: BigDecimal,
                           `Total`: BigDecimal,
                           `FillType`: String,
                           `OrderType`: String)

}
