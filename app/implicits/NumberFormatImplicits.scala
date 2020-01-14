package implicits

import play.api.libs.json.{Format, JsString, JsSuccess, JsValue}

object NumberFormatImplicits {
  implicit object bigDecimalFormat extends Format[BigDecimal] {
    def reads(json: JsValue): JsSuccess[BigDecimal] = {
      val str = json.as[String]
      JsSuccess(BigDecimal(str))
    }

    def writes(bd: BigDecimal) = JsString(bd.toString)
  }

  implicit object bigIntFormat extends Format[BigInt] {
    def reads(json: JsValue): JsSuccess[BigInt] = {
      val str = json.as[String]
      JsSuccess(BigInt(str))
    }

    def writes(bi: BigInt) = JsString(bi.toString)
  }
}
