package implicits

import java.sql.Timestamp
import java.text.SimpleDateFormat

import play.api.libs.json._
import utility.DateUtility

import scala.util.Try

object TimestampImplicits {
  implicit def timestampOrdering: Ordering[Timestamp] =
    (x: Timestamp, y: Timestamp) => x.compareTo(y)

  implicit object timestampFormat extends Format[Timestamp] {
    val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    def reads(json: JsValue): JsSuccess[Timestamp] = {
      val str = json.asOpt[String].getOrElse("N/A")
      Try(JsSuccess(new Timestamp(format.parse(str).getTime)))
        .getOrElse(JsSuccess(DateUtility.now()))
    }

    def writes(ts: Timestamp) = JsString(format.format(ts))
  }
}
