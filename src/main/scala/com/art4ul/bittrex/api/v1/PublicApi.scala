package com.art4ul.bittrex.api.v1

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.art4ul.bittrex.api.SimpleApiV1
import com.art4ul.bittrex.protocol.v1.PublicApiProtocol._
import com.art4ul.bittrex.protocol._
import scala.concurrent.{ExecutionContext, Future}

class PublicApi(implicit system: ActorSystem, ec: ExecutionContext = null, mat: Materializer) extends SimpleApiV1 {

  val Prefix = "public"

  /**
    * Used to get the open and available trading markets at Bittrex along with other meta data.
    */
  def getMarkets: Future[List[Market]] =
    get[List[Market]](s"$Prefix/getmarkets")


  /**
    * Used to get all supported currencies at Bittrex along with other meta data.
    */
  def getCurrencies: Future[List[Currency]] = get[List[Currency]](s"$Prefix/getcurrencies")

  /**
    * Used to get the current tick values for a market.
    *
    * @param market -  literal for the market (ex: BTC-LTC)
    */
  def getTicker(market: String): Future[Ticker] = get[Ticker](s"$Prefix/getticker?market=$market")

  /**
    * Used to get the last 24 hour summary of all active exchanges
    */
  def getMarketSummaries: Future[List[MarketSummary]] =
    get[List[MarketSummary]](s"$Prefix/getmarketsummaries")

  /**
    * Used to get the last 24 hour summary of all active exchanges
    *
    * @param market - literal for the market (ex: BTC-LTC)
    */
  def getMarketSummary(market: String): Future[List[MarketSummary]] =
    get[List[MarketSummary]](s"$Prefix/getmarketsummary?market=$market")


  /**
    * Used to get retrieve the orderbook for a given market
    *
    * @param market - literal for the market (ex: BTC-LTC)
    * @param `type` - buy, sell or both to identify the type of orderbook to return.
    * @return
    */
  def getOrderBook(market: String, `type`: String = "both"): Future[OrderBook] =
    get[OrderBook](s"$Prefix/getorderbook?market=$market&type=${`type`}")

  /**
    * Used to retrieve the latest trades that have occured for a specific market.
    *
    * @param market - literal for the market (ex: BTC-LTC)
    * @return
    */
  def getMarketHistory(market: String): Future[List[Order]] =
    get[List[Order]](s"$Prefix/getmarkethistory?market=$market")


}
