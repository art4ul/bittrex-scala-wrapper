package com.art4ul.bittrex.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import spray.json.{DefaultJsonProtocol, JsValue, JsonReader}

import scala.concurrent.Future
import scala.reflect.ClassTag


object CoreProtocol extends DefaultJsonProtocol with SprayJsonSupport {

  case class BittrexResponse(success: Boolean, message: String, result: JsValue)

  implicit val responseFormat = jsonFormat3(BittrexResponse)
}

object HttpRequestSender {
  val BittrexUrlV1 = "https://bittrex.com/api/v1.1"
  val BittrexUrlV2 = "https://bittrex.com/Api/v2.0"
}

trait HttpRequestSender {

  val baseUrl: String

  protected def sendRequest(uri: Uri)(implicit system: ActorSystem): Future[HttpResponse]

  def targetUrl(uri: Uri): Uri = s"$baseUrl/$uri"
}


class SimpleRequestSender(val baseUrl: String) extends HttpRequestSender {

  override def sendRequest(uri: Uri)(implicit system: ActorSystem): Future[HttpResponse] = {
    val requestUrl = targetUrl(uri)
    println(s"url:$requestUrl")
    val request = HttpRequest().withMethod(HttpMethods.GET).withUri(requestUrl)
    Http().singleRequest(request)
  }
}

class SecureRequestSender(val baseUrl: String, key: String, secret: String) extends HttpRequestSender {

  import com.roundeights.hasher.Implicits._

  override def sendRequest(uri: Uri)(implicit system: ActorSystem): Future[HttpResponse] = {
    def nonce = (System.currentTimeMillis() / 1000).toString

    val requestUrl = targetUrl(uri).withQuery(("apikey" -> key) +: ("nonce" -> nonce) +: uri.query())
    println(s"url:$requestUrl")
    val apisign = requestUrl.toString().hmac(secret).sha512.hex

    val request = HttpRequest()
      .withMethod(HttpMethods.GET)
      .withUri(requestUrl)
      .withHeaders(RawHeader("apisign", apisign))
    Http().singleRequest(request)
  }
}

class SimpleApiV1 extends SimpleRequestSender(HttpRequestSender.BittrexUrlV1) with ResponseHandler

class SimpleApiV2 extends SimpleRequestSender(HttpRequestSender.BittrexUrlV2) with ResponseHandler

class SecureApiV1(key: String, secret: String) extends SecureRequestSender(HttpRequestSender.BittrexUrlV1, key, secret) with ResponseHandler


trait ResponseHandler {
  self: HttpRequestSender =>

  import CoreProtocol._

  protected[api] def get[R: ClassTag](url: Uri)(implicit system: ActorSystem,
                                                mat: Materializer,
                                                formatter: JsonReader[R]): Future[R] = {
    implicit val ec = system.dispatcher
    val response = sendRequest(url)

    response.flatMap {
      case response: HttpResponse if response.status == StatusCodes.OK =>
        val responseFuture = Unmarshal(response).to[BittrexResponse]
        responseFuture.flatMap { resp =>
          if (resp.success) {
            Future.successful(formatter.read(resp.result))
          } else {
            Future.failed(new BittrexException(resp.message))
          }
        }

      case response: HttpResponse if response.status != StatusCodes.OK => Future.failed(new BadRequestException(response))
      case badResponse => Future.failed(new RuntimeException(s"Global exception: $badResponse"))
    }
  }


}

class BittrexException(msg: String) extends RuntimeException(msg)

class BadRequestException(response: HttpResponse) extends RuntimeException(s"Bittrex Exception :${response.toString()}")

