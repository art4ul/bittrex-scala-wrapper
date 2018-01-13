package com.art4ul.bittrex.api.v2

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Query
import akka.stream.Materializer
import com.art4ul.bittrex.api.SimpleApiV2
import com.art4ul.bittrex.protocol.v2.PublicApiProtocol._

import scala.concurrent.{ExecutionContext, Future}

class PublicApi(implicit system: ActorSystem, ec: ExecutionContext = null, mat: Materializer) extends SimpleApiV2 {

  val Prefix = "pub"

  def getTicks(marketName: String, tickInterval: TickInterval.Value): Future[List[Tick]] = {
    val uri = Uri(s"$Prefix/market/GetTicks?_=${System.currentTimeMillis()}")
      .withQuery(
        Query(
          "marketName" -> marketName,
          "tickInterval" -> tickInterval.toString
        ))
    get[List[Tick]](uri)
  }


  def getLatestTick(marketName: String, tickInterval: TickInterval.Value): Future[List[Tick]] = {
    val uri = Uri(s"$Prefix/market/GetLatestTick?_=${System.currentTimeMillis()}")
      .withQuery(
        Query(
          "marketName" -> marketName,
          "tickInterval" -> tickInterval.toString
        ))
    get[List[Tick]](uri)
  }

  def getMarketSummaries: Future[List[GetMarketSummariesItem]] =
    get[List[GetMarketSummariesItem]](s"$Prefix/markets/GetMarketSummaries?_=${System.currentTimeMillis()}")


  def getMarketSummary(marketName: String): Future[MarketSummary] =
    get[MarketSummary](s"$Prefix/market/GetMarketSummary?marketName=$marketName&_=${System.currentTimeMillis()}")


  // Unsupported
//  def getMarketHistory(marketName: String): Future[List[MarketHistory]] =
//    get[List[MarketHistory]](s"$Prefix/market/GetMarketHistory?marketName=$marketName&_=${System.currentTimeMillis()}")

}
