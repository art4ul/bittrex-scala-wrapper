package com.art4ul.bittrex.protocol.v1

import java.time.LocalDateTime
import com.art4ul.bittrex.protocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object PublicApiProtocol extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val marketFormat = jsonFormat8(Market)
  implicit val currencyFormat = jsonFormat7(Currency)
  implicit val tickerFormat = jsonFormat3(Ticker)
  implicit val marketSummaryFormat = jsonFormat14(MarketSummary)
  implicit val shortOrderFormat = jsonFormat2(ShortOrderDescription)
  implicit val orderBookFormat = jsonFormat2(OrderBook)
  implicit val orderFormat = jsonFormat7(Order)

  /*
      "MarketCurrency" : "LTC",
      "BaseCurrency" : "BTC",
      "MarketCurrencyLong" : "Litecoin",
      "BaseCurrencyLong" : "Bitcoin",
      "MinTradeSize" : 0.01000000,
      "MarketName" : "BTC-LTC",
      "IsActive" : true,
      "Created" : "2014-02-13T00:00:00"
    */
  case class Market(`MarketCurrency`: String,
                    `BaseCurrency`: String,
                    `MarketCurrencyLong`: String,
                    `BaseCurrencyLong`: String,
                    `MinTradeSize`: BigDecimal,
                    `MarketName`: String,
                    `IsActive`: Boolean,
                    `Created`: LocalDateTime)


  /*
  {
			"Currency" : "BTC",
			"CurrencyLong" : "Bitcoin",
			"MinConfirmation" : 2,
			"TxFee" : 0.00020000,
			"IsActive" : true,
			"CoinType" : "BITCOIN",
			"BaseAddress" : null
		}
   */
  case class Currency(`Currency`: String,
                      `CurrencyLong`: String,
                      `MinConfirmation`: Int,
                      `TxFee`: BigDecimal,
                      `IsActive`: Boolean,
                      `CoinType`: String,
                      `BaseAddress`: Option[String])


  /*
    "Bid" : 2.05670368,
		"Ask" : 3.35579531,
		"Last" : 3.35579531
   */
  case class Ticker(`Bid`: BigDecimal,
                    `Ask`: BigDecimal,
                    `Last`: BigDecimal)

  /*
  {
			"MarketName" : "BTC-LTC",
			"High" : 0.01350000,
			"Low" : 0.01200000,
			"Volume" : 3833.97619253,
			"Last" : 0.01349998,
			"BaseVolume" : 47.03987026,
			"TimeStamp" : "2014-07-09T07:22:16.72",
			"Bid" : 0.01271001,
			"Ask" : 0.01291100,
			"OpenBuyOrders" : 45,
			"OpenSellOrders" : 45,
			"PrevDay" : 0.01229501,
			"Created" : "2014-02-13T00:00:00",
			"DisplayMarketName" : null
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
                           `OpenBuyOrders`: Int,
                           `OpenSellOrders`: Int,
                           `PrevDay`: BigDecimal,
                           `Created`: LocalDateTime,
                           `DisplayMarketName`: Option[String])

  /*
  {
		"buy" : [{
				"Quantity" : 12.37000000,
				"Rate" : 0.02525000
			}
		],
		"sell" : [{
				"Quantity" : 32.55412402,
				"Rate" : 0.02540000
			}, {
				"Quantity" : 60.00000000,
				"Rate" : 0.02550000
			}, {
				"Quantity" : 60.00000000,
				"Rate" : 0.02575000
			}, {
				"Quantity" : 84.00000000,
				"Rate" : 0.02600000
			}
		]
	}
   */
  case class OrderBook(buy: List[ShortOrderDescription], sell: List[ShortOrderDescription])

  /*
    {
      "Quantity" : 12.37000000,
			"Rate" : 0.02525000
		}
   */
  case class ShortOrderDescription(`Quantity`: BigDecimal, `Rate`: BigDecimal)

  /*
  {
			"Id" : 319435,
			"TimeStamp" : "2014-07-09T03:21:20.08",
			"Quantity" : 0.30802438,
			"Price" : 0.01263400,
			"Total" : 0.00389158,
			"FillType" : "FILL",
			"OrderType" : "BUY"
		}
   */
  case class Order(`Id`: Long,
                   `TimeStamp`: LocalDateTime,
                   `Quantity`: BigDecimal,
                   `Price`: BigDecimal,
                   `Total`: BigDecimal,
                   `FillType`: String,
                   `OrderType`: String)

}