package com.art4ul.bittrex

import spray.json.{JsString, JsValue, RootJsonFormat}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import spray.json.DefaultJsonProtocol._

package object protocol {

  implicit object DateJsonFormat extends RootJsonFormat[LocalDateTime] {

    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    override def write(obj: LocalDateTime) = JsString(obj.format(formatter))

    override def read(json: JsValue): LocalDateTime = json match {
      case JsString(s) => LocalDateTime.parse(s, formatter)
      case _ => throw new Exception("Malformed datetime")
    }
  }

  implicit val uuidFormat = jsonFormat1(UuidResponse)

  case class UuidResponse(uuid: String)
}

