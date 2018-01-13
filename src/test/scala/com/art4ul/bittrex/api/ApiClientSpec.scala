package com.art4ul.bittrex.api

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.stream.ActorMaterializer
import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ApiClientSpec extends TestKit(ActorSystem("MySpec"))
  with FlatSpecLike
  with Matchers
  with BeforeAndAfterAll {

  implicit val execContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  it should "return value from 'result' field if 'success' field is true " in {
    val api = MockedApi(
      """
        |{
        |	"success" : true,
        |	"message" : "",
        |	"result" : 42
        | }
      """.stripMargin)
    Await.result(api.get[Int](""), Duration.Inf) shouldBe 42
  }


  it should "throw BittrexException if 'success' field is false " in {
    val api = MockedApi(
      """
        |{
        |	"success" : false,
        |	"message" : "",
        |	"result" : 42
        | }
      """.stripMargin)
    intercept[BittrexException] {
      Await.result(api.get[Int](""), Duration.Inf)
    }
  }

  it should "throw BadRequestException if 'success' field is false " in {
    val api = new MockedApi(HttpResponse(status = StatusCodes.ServiceUnavailable))
    intercept[BadRequestException] {
      Await.result(api.get[Int](""), Duration.Inf)
    }
  }

}
