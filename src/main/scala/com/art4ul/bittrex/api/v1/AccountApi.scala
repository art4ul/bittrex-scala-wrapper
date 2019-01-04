package com.art4ul.bittrex.api.v1

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Query
import akka.stream.Materializer
import com.art4ul.bittrex.api.SecureApiV1
import com.art4ul.bittrex.protocol._
import com.art4ul.bittrex.protocol.v1.AccountApiProtocol._
import com.art4ul.bittrex.util.DecimalUtil

import scala.concurrent.Future

class AccountApi(key: String, secret: String)(implicit system: ActorSystem, mat: Materializer) extends SecureApiV1(key, secret) {

  private val Prefix = "account"

  /**
    * Used to retrieve all balances from your account
    */
  def getBalances: Future[List[Balance]] = get[List[Balance]](s"$Prefix/getbalances")

  /**
    * Used to retrieve the balance from your account for a specific currency.
    *
    * @param currency literal for the currency (ex: LTC)
    */
  def getBalance(currency: String): Future[Balance] =
    get[Balance](Uri(s"$Prefix/getbalance").withQuery(Query("currency" -> currency)))

  /**
    * Used to retrieve or generate an address for a specific currency.
    * If one does not exist, the call will fail and return ADDRESS_GENERATING until one is available.
    *
    * @param currency - literal for the currency (ie. BTC)
    */
  def getDepositAddress(currency: String): Future[DepositAddress] =
    get[DepositAddress](Uri(s"$Prefix/getdepositaddress").withQuery(Query("currency" -> currency)))

  /**
    * Used to withdraw funds from your account. note: please account for txfee.
    *
    * @param currency  - literal for the currency (ie. BTC)
    * @param quantity  - quantity of coins to withdraw
    * @param address   - address where to send the funds.
    * @param paymentid - optional	used for CryptoNotes/BitShareX/Nxt optional field (memo/paymentid)
    */
  def withdraw(currency: String, quantity: BigDecimal, address: String, paymentid: Option[String] = None): Future[UuidResponse] = {
    val params = Map(
      "currency" -> currency,
      "quantity" -> DecimalUtil.toStringFormat(quantity)
    ) ++ paymentid.map(pid => "paymentid" -> pid)

    get[UuidResponse](Uri(s"$Prefix/withdraw").withQuery(Query(params)))
  }

  /**
    * Used to retrieve a single order by uuid.
    *
    * @param uuid - the uuid of the buy or sell order
    */
  def getOrder(uuid: String): Future[FullOrderDescription] =
    get[FullOrderDescription](Uri(s"$Prefix/getorder").withQuery(Query("uuid" -> uuid)))

  /**
    * Used to retrieve your order history.
    *
    * @param market -  literal for the market (ie. BTC-LTC). If ommited, will return for all markets
    */
  def getOrderHistory(market: Option[String] = None): Future[List[OrderHistory]] = {
    val param = market.map(m => "market" -> m).toMap
    get[List[OrderHistory]](Uri(s"$Prefix/getorderhistory").withQuery(Query(param)))
  }


  /**
    * Used to retrieve your withdrawal history.
    *
    * @param currency - string literal for the currecy (ie. BTC). If omitted, will return for all currencies
    */
  def getWithdrawalHistory(currency: Option[String] = None): Future[List[WithdrawalHistory]] = {
    val param = currency.map(m => "currency" -> m).toMap
    get[List[WithdrawalHistory]](Uri(s"$Prefix/getwithdrawalhistory").withQuery(Query(param)))
  }


  /**
    * Used to retrieve your deposit history.
    *
    * @param currency - string literal for the currecy (ie. BTC). If omitted, will return for all currencies
    */
  def getDepositHistory(currency: Option[String] = None): Future[List[WithdrawalHistory]] = {
    val param = currency.map(m => "currency" -> m).toMap
    get[List[WithdrawalHistory]](Uri(s"$Prefix/getdeposithistory").withQuery(Query(param)))
  }

}
