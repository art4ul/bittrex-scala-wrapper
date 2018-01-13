package com.art4ul.bittrex.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtil {

  implicit def stringToLocalDate(str:String):LocalDateTime =
    LocalDateTime.parse(str,DateTimeFormatter.ISO_DATE_TIME)
}
