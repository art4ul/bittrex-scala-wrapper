package com.art4ul.bittrex.util

import java.text.DecimalFormat

import scala.math.BigDecimal.RoundingMode
import scala.math.BigDecimal.RoundingMode.RoundingMode

object DecimalUtil extends App {

  def toStringFormat(value: BigDecimal, fraction: Int = 8, mode: RoundingMode = RoundingMode.DOWN): String = {
    val tval = value.setScale(fraction, mode)
    val df = new DecimalFormat();
    df.setMaximumFractionDigits(fraction)
    df.setMinimumFractionDigits(0)
    df.setGroupingUsed(false)
    df.format(tval)
  }

}
