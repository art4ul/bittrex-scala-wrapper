package com.art4ul.bittrex.protocol.v1

import java.time.LocalDateTime

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.art4ul.bittrex.protocol._
import spray.json.DefaultJsonProtocol

object MarketApiProtocol extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val openOrderFormat = jsonFormat17(OpenOrder)

  /*
      "Uuid" : null,
			"OrderUuid" : "09aa5bb6-8232-41aa-9b78-a5a1093e0211",
			"Exchange" : "BTC-LTC",
			"OrderType" : "LIMIT_SELL",
			"Quantity" : 5.00000000,
			"QuantityRemaining" : 5.00000000,
			"Limit" : 2.00000000,
			"CommissionPaid" : 0.00000000,
			"Price" : 0.00000000,
			"PricePerUnit" : null,
			"Opened" : "2014-07-09T03:55:48.77",
			"Closed" : null,
			"CancelInitiated" : false,
			"ImmediateOrCancel" : false,
			"IsConditional" : false,
			"Condition" : null,
			"ConditionTarget" : null
   */

  case class OpenOrder(`Uuid`: Option[String],
                       `OrderUuid`: String,
                       `Exchange`: String,
                       `OrderType`: String,
                       `Quantity`: BigDecimal,
                       `QuantityRemaining`: BigDecimal,
                       `Limit`: BigDecimal,
                       `CommissionPaid`: BigDecimal,
                       `Price`: BigDecimal,
                       `PricePerUnit`: Option[BigDecimal],
                       `Opened`: LocalDateTime,
                       `Closed`: Option[LocalDateTime],
                       `CancelInitiated`: Boolean,
                       `ImmediateOrCancel`: Boolean,
                       `IsConditional`: Boolean,
                       `Condition`: Option[String],
                       `ConditionTarget`: Option[BigDecimal])

}
