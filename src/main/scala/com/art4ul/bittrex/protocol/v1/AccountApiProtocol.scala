package com.art4ul.bittrex.protocol.v1

import java.time.LocalDateTime
import com.art4ul.bittrex.protocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object AccountApiProtocol extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val balanceFormat = jsonFormat7(Balance)
  implicit val depositAddressFormat = jsonFormat2(DepositAddress)
  implicit val fullOrderDescriptionFormat = jsonFormat21(FullOrderDescription)
  implicit val orderHistoryFormat = jsonFormat14(OrderHistory)
  implicit val withdrawalHistoryFormat = jsonFormat11(WithdrawalHistory)

  /*
      {
			"Currency" : "DOGE",
			"Balance" : 0.00000000,
			"Available" : 0.00000000,
			"Pending" : 0.00000000,
			"CryptoAddress" : "DLxcEt3AatMyr2NTatzjsfHNoB9NT62HiF",
			"Requested" : false,
			"Uuid" : null
		}
    */
  case class Balance(`Currency`: String,
                     `Balance`: BigDecimal,
                     `Available`: BigDecimal,
                     `Pending`: BigDecimal,
                     `CryptoAddress`: Option[String],
                     `Requested`: Option[Boolean],
                     `Uuid`: Option[String])


  /*
    {
      "Currency" : "VTC",
      "Address" : "Vy5SKeKGXUHKS2WVpJ76HYuKAu3URastUo"
    }
   */
  case class DepositAddress(`Currency`: String, `Address`: Option[String])

  /*
    {
      "AccountId" : null,
      "OrderUuid" : "0cb4c4e4-bdc7-4e13-8c13-430e587d2cc1",
      "Exchange" : "BTC-SHLD",
      "Type" : "LIMIT_BUY",
      "Quantity" : 1000.00000000,
      "QuantityRemaining" : 1000.00000000,
      "Limit" : 0.00000001,
      "Reserved" : 0.00001000,
      "ReserveRemaining" : 0.00001000,
      "CommissionReserved" : 0.00000002,
      "CommissionReserveRemaining" : 0.00000002,
      "CommissionPaid" : 0.00000000,
      "Price" : 0.00000000,
      "PricePerUnit" : null,
      "Opened" : "2014-07-13T07:45:46.27",
      "Closed" : null,
      "IsOpen" : true,
      "Sentinel" : "6c454604-22e2-4fb4-892e-179eede20972",
      "CancelInitiated" : false,
      "ImmediateOrCancel" : false,
      "IsConditional" : false,
      "Condition" : "NONE",
      "ConditionTarget" : null
    }
   */

  case class FullOrderDescription(`AccountId`: Option[String],
                                  `OrderUuid`: String,
                                  `Exchange`: String,
                                  `Type`: String,
                                  `Quantity`: BigDecimal,
                                  `QuantityRemaining`: BigDecimal,
                                  `Limit`: BigDecimal,
                                  `Reserved`: BigDecimal,
                                  `ReserveRemaining`: BigDecimal,
                                  `CommissionReserved`: BigDecimal,
                                  `CommissionReserveRemaining`: BigDecimal,
                                  `CommissionPaid`: BigDecimal,
                                  `Price`: BigDecimal,
                                  `PricePerUnit`: Option[BigDecimal],
                                  `Opened`: LocalDateTime,
                                  `Closed`: Option[LocalDateTime],
                                  `IsOpen`: Boolean,
                                  `Sentinel`: String,
                                  `CancelInitiated`: Boolean,
                                  `ImmediateOrCancel`: Boolean,
                                  `IsConditional` : Boolean)


  /*
    {
        "OrderUuid" : "fd97d393-e9b9-4dd1-9dbf-f288fc72a185",
        "Exchange" : "BTC-LTC",
        "TimeStamp" : "2014-07-09T04:01:00.667",
        "OrderType" : "LIMIT_BUY",
        "Limit" : 0.00000001,
        "Quantity" : 100000.00000000,
        "QuantityRemaining" : 100000.00000000,
        "Commission" : 0.00000000,
        "Price" : 0.00000000,
        "PricePerUnit" : null,
        "IsConditional" : false,
        "Condition" : null,
        "ConditionTarget" : null,
        "ImmediateOrCancel" : false
      }
   */
  case class OrderHistory(`OrderUuid`: String,
                          `Exchange`: String,
                          `TimeStamp`: LocalDateTime,
                          `OrderType`: String,
                          `Limit`: BigDecimal,
                          `Quantity`: BigDecimal,
                          `QuantityRemaining`: BigDecimal,
                          `Commission`: BigDecimal,
                          `Price`: BigDecimal,
                          `PricePerUnit`: Option[BigDecimal],
                          `IsConditional`: Boolean,
                          `Condition`: Option[String],
                          `ConditionTarget`: Option[BigDecimal],
                          `ImmediateOrCancel`: Boolean)

  /*
  {
			"PaymentUuid" : "b52c7a5c-90c6-4c6e-835c-e16df12708b1",
			"Currency" : "BTC",
			"Amount" : 17.00000000,
			"Address" : "1DeaaFBdbB5nrHj87x3NHS4onvw1GPNyAu",
			"Opened" : "2014-07-09T04:24:47.217",
			"Authorized" : true,
			"PendingPayment" : false,
			"TxCost" : 0.00020000,
			"TxId" : null,
			"Canceled" : true,
			"InvalidAddress" : false
		}
   */
  case class WithdrawalHistory(`PaymentUuid`: String,
                               `Currency`: String,
                               `Amount`: BigDecimal,
                               `Address`: String,
                               `Opened`: LocalDateTime,
                               `Authorized`: Boolean,
                               `PendingPayment`: Boolean,
                               `TxCost`: BigDecimal,
                               `TxId`: Option[String],
                               `Canceled`: Boolean,
                               `InvalidAddress`: Boolean)

}
