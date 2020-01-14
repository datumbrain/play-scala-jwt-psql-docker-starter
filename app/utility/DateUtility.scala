package utility

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone}

object DateUtility {
  private val DATE_FORMAT = "dd/MM/yyyy hh:mm:ss.SSS a"

  private def utcTime: Long = {
    val dateFormatterUTC = new SimpleDateFormat(DATE_FORMAT)
    dateFormatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"))

    val currentTime = Calendar.getInstance
    val timeString = dateFormatterUTC.format(currentTime.getTime)
    val dateFormatLocal = new SimpleDateFormat(DATE_FORMAT)

    dateFormatLocal.parse(timeString).getTime
  }

  def now(): Timestamp = new Timestamp(utcTime)
}
