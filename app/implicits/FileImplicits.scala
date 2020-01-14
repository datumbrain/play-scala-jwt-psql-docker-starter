package implicits

import java.nio.charset.CodingErrorAction

import scala.io.Codec

object FileImplicits {
  implicit object malformedFileText {
    implicit val codec: Codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
  }
}
