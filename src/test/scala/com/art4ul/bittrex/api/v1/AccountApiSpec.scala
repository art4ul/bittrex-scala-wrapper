package com.art4ul.bittrex.api.v1

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.stream.ActorMaterializer
import akka.testkit.TestKit
import com.art4ul.bittrex.api.MockedRequestSender
import com.art4ul.bittrex.protocol.v1.AccountApiProtocol._
import com.art4ul.bittrex.protocol._
import com.art4ul.bittrex.protocol.v1.AccountApiProtocol.Balance
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}
import com.art4ul.bittrex.util.DateUtil._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class AccountApiSpec extends TestKit(ActorSystem("AccountApiSpec"))
  with FlatSpecLike
  with Matchers
  with BeforeAndAfterAll {

  implicit val materializer = ActorMaterializer()

  class AccountApiMock(val mockedResponse: HttpResponse) extends AccountApi("", "") with MockedRequestSender

  object AccountApiMock {
    def apply(body: String): AccountApiMock = new AccountApiMock(HttpResponse(
      entity = HttpEntity(string = body, contentType = ContentTypes.`application/json`))
    )
  }


  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "getBalances" should "return list of markets " in {
    val api = AccountApiMock(
      """
       {
       |	"success" : true,
       |	"message" : "",
       |	"result" : [{
       |			"Currency" : "DOGE",
       |			"Balance" : 0.00000000,
       |			"Available" : 0.00000000,
       |			"Pending" : 0.00000000,
       |			"CryptoAddress" : "DLxcEt3AatMyr2NTatzjsfHNoB9NT62HiF",
       |			"Requested" : false,
       |			"Uuid" : null
       |
       |		}, {
       |			"Currency" : "BTC",
       |			"Balance" : 14.21549076,
       |			"Available" : 14.21549076,
       |			"Pending" : 0.00000000,
       |			"CryptoAddress" : "1Mrcdr6715hjda34pdXuLqXcju6qgwHA31",
       |			"Requested" : false,
       |			"Uuid" : null
       |		}
       |	]
       |}
      """.stripMargin)
    val result = Await.result(api.getBalances, Duration.Inf)
    val expected = List(
      Balance(
        `Currency` = "DOGE",
        `Balance` = 0.00000000,
        `Available` = 0.00000000,
        `Pending` = 0.00000000,
        `CryptoAddress` = Some("DLxcEt3AatMyr2NTatzjsfHNoB9NT62HiF"),
        `Requested` = Some(false),
        `Uuid` = None)
      ,
      Balance(
        `Currency` = "BTC",
        `Balance` = 14.21549076,
        `Available` = 14.21549076,
        `Pending` = 0.00000000,
        `CryptoAddress` = Some("1Mrcdr6715hjda34pdXuLqXcju6qgwHA31"),
        `Requested` = Some(false),
        `Uuid` = None
      )
    )
    result shouldBe expected
  }

  "getBalance" should "return list of markets " in {
    val api = AccountApiMock(
      """
      {
       |	"success" : true,
       |	"message" : "",
       |	"result" : {
       |		"Currency" : "BTC",
       |		"Balance" : 4.21549076,
       |		"Available" : 4.21549076,
       |		"Pending" : 0.00000000,
       |		"CryptoAddress" : "1MacMr6715hjds342dXuLqXcju6fgwHA31",
       |		"Requested" : false,
       |		"Uuid" : null
       |	}
       |}
      """.stripMargin)
    val result = Await.result(api.getBalance("BTC"), Duration.Inf)
    val expected = Balance(
        `Currency` = "BTC",
        `Balance` = 4.21549076,
        `Available` = 4.21549076,
        `Pending` = 0.00000000,
        `CryptoAddress` = Some("1MacMr6715hjds342dXuLqXcju6fgwHA31"),
        `Requested` = Some(false),
        `Uuid` = None
      )
    result shouldBe expected
  }

  "getDepositAddress" should "return list of markets " in {
    val api = AccountApiMock(
      """
     {
     |	"success" : true,
     |	"message" : "",
     |	"result" : {
     |		"Currency" : "VTC",
     |		"Address" : "Vy5SKeKGXUHKS2WVpJ76HYuKAu3URastUo"
     |	}
     |}
      """.stripMargin)
    val result = Await.result(api.getDepositAddress("BTC"), Duration.Inf)
    val expected = DepositAddress(
      `Currency` = "VTC",
      `Address` = Some("Vy5SKeKGXUHKS2WVpJ76HYuKAu3URastUo")
    )
    result shouldBe expected
  }


  "withdraw" should "return list of markets " in {
    val api = AccountApiMock(
      """{
        |	"success" : true,
        |	"message" : "",
        |	"result" : {
        |			"uuid" : "68b5a16c-92de-11e3-ba3b-425861b86ab6"
        |	}
        |}
      """.stripMargin)
    val result = Await.result(api.withdraw("BTC", BigDecimal(1), "123"), Duration.Inf)
    val expected = UuidResponse("68b5a16c-92de-11e3-ba3b-425861b86ab6")
    result shouldBe expected
  }

  "getOrder" should "return list of markets " in {
    val api = AccountApiMock(
      """{
        |	"success" : true,
        |	"message" : "",
        |	"result" : {
        |		"AccountId" : null,
        |		"OrderUuid" : "0cb4c4e4-bdc7-4e13-8c13-430e587d2cc1",
        |		"Exchange" : "BTC-SHLD",
        |		"Type" : "LIMIT_BUY",
        |		"Quantity" : 1000.00000000,
        |		"QuantityRemaining" : 1000.00000000,
        |		"Limit" : 0.00000001,
        |		"Reserved" : 0.00001000,
        |		"ReserveRemaining" : 0.00001000,
        |		"CommissionReserved" : 0.00000002,
        |		"CommissionReserveRemaining" : 0.00000002,
        |		"CommissionPaid" : 0.00000000,
        |		"Price" : 0.00000000,
        |		"PricePerUnit" : null,
        |		"Opened" : "2014-07-13T07:45:46.27",
        |		"Closed" : null,
        |		"IsOpen" : true,
        |		"Sentinel" : "6c454604-22e2-4fb4-892e-179eede20972",
        |		"CancelInitiated" : false,
        |		"ImmediateOrCancel" : false,
        |		"IsConditional" : false,
        |		"Condition" : "NONE",
        |		"ConditionTarget" : null
        |	}
        |}
      """.stripMargin)
    val result = Await.result(api.getOrder("uid"), Duration.Inf)
    val expected = FullOrderDescription(
      `AccountId` = None,
      `OrderUuid` = "0cb4c4e4-bdc7-4e13-8c13-430e587d2cc1",
      `Exchange` = "BTC-SHLD",
      `Type` = "LIMIT_BUY",
      `Quantity` = 1000.00000000,
      `QuantityRemaining` = 1000.00000000,
      `Limit` = 0.00000001,
      `Reserved` = 0.00001000,
      `ReserveRemaining` = 0.00001000,
      `CommissionReserved` = 0.00000002,
      `CommissionReserveRemaining` = 0.00000002,
      `CommissionPaid` = 0.00000000,
      `Price` = 0.00000000,
      `PricePerUnit` = None,
      `Opened` = "2014-07-13T07:45:46.27",
      `Closed` = None,
      `IsOpen` = true,
      `Sentinel` = "6c454604-22e2-4fb4-892e-179eede20972",
      `CancelInitiated` = false,
      `ImmediateOrCancel` = false,
      `IsConditional` = false
    )
    result shouldBe expected
  }

  "getOrderHistory" should "return list of markets " in {
    val api = AccountApiMock(
      """{
        |	"success" : true,
        |	"message" : "",
        |	"result" : [{
        |			"OrderUuid" : "fd97d393-e9b9-4dd1-9dbf-f288fc72a185",
        |			"Exchange" : "BTC-LTC",
        |			"TimeStamp" : "2014-07-09T04:01:00.667",
        |			"OrderType" : "LIMIT_BUY",
        |			"Limit" : 0.00000001,
        |			"Quantity" : 100000.00000000,
        |			"QuantityRemaining" : 100000.00000000,
        |			"Commission" : 0.00000000,
        |			"Price" : 0.00000000,
        |			"PricePerUnit" : null,
        |			"IsConditional" : false,
        |			"Condition" : null,
        |			"ConditionTarget" : null,
        |			"ImmediateOrCancel" : false
        |		}, {
        |			"OrderUuid" : "17fd64d1-f4bd-4fb6-adb9-42ec68b8697d",
        |			"Exchange" : "BTC-ZS",
        |			"TimeStamp" : "2014-07-08T20:38:58.317",
        |			"OrderType" : "LIMIT_SELL",
        |			"Limit" : 0.00002950,
        |			"Quantity" : 667.03644955,
        |			"QuantityRemaining" : 0.00000000,
        |			"Commission" : 0.00004921,
        |			"Price" : 0.01968424,
        |			"PricePerUnit" : 0.00002950,
        |			"IsConditional" : false,
        |			"Condition" : null,
        |			"ConditionTarget" : null,
        |			"ImmediateOrCancel" : false
        |		}
        |	]
        |}
      """.stripMargin)
    val result = Await.result(api.getOrderHistory(), Duration.Inf)
    val expected = List(OrderHistory(
      `OrderUuid` = "fd97d393-e9b9-4dd1-9dbf-f288fc72a185",
      `Exchange` = "BTC-LTC",
      `TimeStamp` = "2014-07-09T04:01:00.667",
      `OrderType` = "LIMIT_BUY",
      `Limit` = 0.00000001,
      `Quantity` = 100000.00000000,
      `QuantityRemaining` = 100000.00000000,
      `Commission` = 0.00000000,
      `Price` = 0.00000000,
      `PricePerUnit` = None,
      `IsConditional` = false,
      `Condition` = None,
      `ConditionTarget` = None,
      `ImmediateOrCancel` = false),
      OrderHistory(
        `OrderUuid` = "17fd64d1-f4bd-4fb6-adb9-42ec68b8697d",
        `Exchange` = "BTC-ZS",
        `TimeStamp` = "2014-07-08T20:38:58.317",
        `OrderType` = "LIMIT_SELL",
        `Limit` = 0.00002950,
        `Quantity` = 667.03644955,
        `QuantityRemaining` = 0.00000000,
        `Commission` = 0.00004921,
        `Price` = 0.01968424,
        `PricePerUnit` = Some(0.00002950),
        `IsConditional` = false,
        `Condition` = None,
        `ConditionTarget` = None,
        `ImmediateOrCancel` = false
      )
    )
    result shouldBe expected
  }

  "getDepositHistory" should "return list of markets " in {
    val api = AccountApiMock(
      """{
        |	"success" : true,
        |	"message" : "",
        |	"result" : [{
        |			"PaymentUuid" : "554ec664-8842-4fe9-b491-06225becbd59",
        |			"Currency" : "BTC",
        |			"Amount" : 0.00156121,
        |			"Address" : "1K37yQZaGrPKNTZ5KNP792xw8f7XbXxetE",
        |			"Opened" : "2014-07-11T03:41:25.323",
        |			"Authorized" : true,
        |			"PendingPayment" : false,
        |			"TxCost" : 0.00020000,
        |			"TxId" : "70cf6fdccb9bd38e1a930e13e4ae6299d678ed6902da710fa3cc8d164f9be126",
        |			"Canceled" : false,
        |			"InvalidAddress" : false
        |		}, {
        |			"PaymentUuid" : "d3fdf168-3d8e-40b6-8fe4-f46e2a7035ea",
        |			"Currency" : "BTC",
        |			"Amount" : 0.11800000,
        |			"Address" : "1Mrcar6715hjds34pdXuLqXcju6QgwHA31",
        |			"Opened" : "2014-07-03T20:27:07.163",
        |			"Authorized" : true,
        |			"PendingPayment" : false,
        |			"TxCost" : 0.00020000,
        |			"TxId" : "3efd41b3a051433a888eed3ecc174c1d025a5e2b486eb418eaaec5efddda22de",
        |			"Canceled" : false,
        |			"InvalidAddress" : false
        |		}
        |    ]
        |}
      """.stripMargin)
    val result = Await.result(api.getDepositHistory(), Duration.Inf)
    val expected = List(
      WithdrawalHistory(
        `PaymentUuid` = "554ec664-8842-4fe9-b491-06225becbd59",
        `Currency` = "BTC",
        `Amount` = 0.00156121,
        `Address` = "1K37yQZaGrPKNTZ5KNP792xw8f7XbXxetE",
        `Opened` = "2014-07-11T03:41:25.323",
        `Authorized` = true,
        `PendingPayment` = false,
        `TxCost` = 0.00020000,
        `TxId` = Some("70cf6fdccb9bd38e1a930e13e4ae6299d678ed6902da710fa3cc8d164f9be126"),
        `Canceled` = false,
        `InvalidAddress` = false
      ),
      WithdrawalHistory(
        `PaymentUuid` = "d3fdf168-3d8e-40b6-8fe4-f46e2a7035ea",
        `Currency` = "BTC",
        `Amount` = 0.11800000,
        `Address` = "1Mrcar6715hjds34pdXuLqXcju6QgwHA31",
        `Opened` = "2014-07-03T20:27:07.163",
        `Authorized` = true,
        `PendingPayment` = false,
        `TxCost` = 0.00020000,
        `TxId` = Some("3efd41b3a051433a888eed3ecc174c1d025a5e2b486eb418eaaec5efddda22de"),
        `Canceled` = false,
        `InvalidAddress` = false
      )
    )
    result shouldBe expected
  }

  "getWithdrawalHistory" should "return list of markets " in {
    val api = AccountApiMock(
      """{
        |	"success" : true,
        |	"message" : "",
        |	"result" : [{
        |			"PaymentUuid" : "b52c7a5c-90c6-4c6e-835c-e16df12708b1",
        |			"Currency" : "BTC",
        |			"Amount" : 17.00000000,
        |			"Address" : "1DeaaFBdbB5nrHj87x3NHS4onvw1GPNyAu",
        |			"Opened" : "2014-07-09T04:24:47.217",
        |			"Authorized" : true,
        |			"PendingPayment" : false,
        |			"TxCost" : 0.00020000,
        |			"TxId" : null,
        |			"Canceled" : true,
        |			"InvalidAddress" : false
        |		}, {
        |			"PaymentUuid" : "f293da98-788c-4188-a8f9-8ec2c33fdfcf",
        |			"Currency" : "XC",
        |			"Amount" : 7513.75121715,
        |			"Address" : "XVnSMgAd7EonF2Dgc4c9K14L12RBaW5S5J",
        |			"Opened" : "2014-07-08T23:13:31.83",
        |			"Authorized" : true,
        |			"PendingPayment" : false,
        |			"TxCost" : 0.00002000,
        |			"TxId" : "b4a575c2a71c7e56d02ab8e26bb1ef0a2f6cf2094f6ca2116476a569c1e84f6e",
        |			"Canceled" : false,
        |			"InvalidAddress" : false
        |		}
        |	]
        |}
      """.stripMargin)
    val result = Await.result(api.getWithdrawalHistory(), Duration.Inf)
    val expected = List(
      WithdrawalHistory(
        `PaymentUuid` = "b52c7a5c-90c6-4c6e-835c-e16df12708b1",
        `Currency` = "BTC",
        `Amount` = 17.00000000,
        `Address` = "1DeaaFBdbB5nrHj87x3NHS4onvw1GPNyAu",
        `Opened` = "2014-07-09T04:24:47.217",
        `Authorized` = true,
        `PendingPayment` = false,
        `TxCost` = 0.00020000,
        `TxId` = None,
        `Canceled` = true,
        `InvalidAddress` = false
      ),
      WithdrawalHistory(
        `PaymentUuid` = "f293da98-788c-4188-a8f9-8ec2c33fdfcf",
        `Currency` = "XC",
        `Amount` = 7513.75121715,
        `Address` = "XVnSMgAd7EonF2Dgc4c9K14L12RBaW5S5J",
        `Opened` = "2014-07-08T23:13:31.83",
        `Authorized` = true,
        `PendingPayment` = false,
        `TxCost` = 0.00002000,
        `TxId` = Some("b4a575c2a71c7e56d02ab8e26bb1ef0a2f6cf2094f6ca2116476a569c1e84f6e"),
        `Canceled` = false,
        `InvalidAddress` = false
      )
    )
    result shouldBe expected
  }

}
