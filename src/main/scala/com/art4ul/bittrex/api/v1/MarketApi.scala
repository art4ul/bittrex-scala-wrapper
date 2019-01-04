package com.art4ul.bittrex.api.v1

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Query
import akka.stream.Materializer
import com.art4ul.bittrex.api.SecureApiV1
import com.art4ul.bittrex.protocol._
import com.art4ul.bittrex.protocol.v1.MarketApiProtocol._
import com.art4ul.bittrex.util.DecimalUtil

import scala.concurrent.Future

class MarketApi(key: String, secret: String)(implicit system: ActorSystem,
                                             mat: Materializer) extends SecureApiV1(key, secret) {

  private val Prefix = "market"

  /**
    * Used to place a buy order in a specific market. Use buylimit to place limit orders.
    * Make sure you have the proper permissions set on your API keys for this call to work
    *
    * @param market   - string literal for the market (ex: BTC-LTC)
    * @param quantity - the amount to purchase
    * @param rate     - the rate at which to place the order.
    */
  def buyLimit(market: String, quantity: BigDecimal, rate: BigDecimal): Future[UuidResponse] = {
    val params = Map(
      "market" -> market,
      "quantity" -> DecimalUtil.toStringFormat(quantity),
      "rate" -> DecimalUtil.toStringFormat(rate)
    )
    get[UuidResponse](Uri(s"$Prefix/buylimit").withQuery(Query(params)))
  }


  /**
    * Used to place an sell order in a specific market. Use selllimit to place limit orders.
    * Make sure you have the proper permissions set on your API keys for this call to work
    *
    * @param market   - string literal for the market (ex: BTC-LTC)
    * @param quantity - the amount to purchase
    * @param rate     - the rate at which to place the order.
    */
  def sellLimit(market: String, quantity: BigDecimal, rate: BigDecimal): Future[UuidResponse] = {
    val params = Map(
      "market" -> market,
      "quantity" -> DecimalUtil.toStringFormat(quantity),
      "rate" -> DecimalUtil.toStringFormat(rate)
    )
    get[UuidResponse](Uri(s"$Prefix/selllimit").withQuery(Query(params)))
  }


  /**
    * Used to cancel a buy or sell order.
    *
    * @param uuid - required	uuid of buy or sell order
    */
  def cancel(uuid: String): Future[Unit] =
    get[Unit](Uri(s"$Prefix/cancel").withQuery(Query("uuid" -> uuid)))


  /**
    * Get all orders that you currently have opened. A specific market can be requested
    *
    * @param market - a string literal for the market (ie. BTC-LTC)
    */
  def getOpenOrders(market: String): Future[List[OpenOrder]] =
    get[List[OpenOrder]](Uri(s"$Prefix/getopenorders").withQuery(Query("market" -> market)))

}
