package models
import java.net.URLEncoder
import play.api.Logger

object Util {
  private val LOG = Logger(getClass)
  
  /**
   * 
   */
  val USER_ID = "USER_ID"

    /**
     * 
     */
  def buildEncodedQueryString(params: List[(String, AnyVal)]): String = {
    val encoded = for {
      (name, value) <- params if value != None
      encodedValue = value match {
        case x => URLEncoder.encode(x.toString, "UTF8")
      }
    } yield name + "=" + encodedValue

    encoded.mkString("?", "&", "")
  }

  /**
   * 
   */
  def measureTime[A](message: String)(a: => A) = {
    val now = System.nanoTime
    val result = a
    val micros = (System.nanoTime - now) / 1000
    LOG.debug("%s: [%d] microseconds".format(message, micros))
    result
  }

}