package com.art4ul.bittrex.api

import akka.actor.ActorSystem
import akka.http.scaladsl.model._

import scala.concurrent.Future

trait MockedRequestSender extends HttpRequestSender {

  val mockedResponse: HttpResponse

  override val baseUrl: String = ""

  override def sendRequest(uri: Uri)(implicit system: ActorSystem): Future[HttpResponse] =
    Future.successful(mockedResponse)
}


class MockedApi(val mockedResponse: HttpResponse) extends MockedRequestSender with ResponseHandler

object MockedApi{
  def apply(body:String): MockedApi = new MockedApi(HttpResponse(
    entity = HttpEntity(string = body, contentType = ContentTypes.`application/json`))
  )
}

