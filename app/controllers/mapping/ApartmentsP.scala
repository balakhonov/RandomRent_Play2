package controllers.mapping

import java.util.Date
import models.db.mapping.Apartment
import play.api.libs.json.Format
import play.api.libs.json.JsResult
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import play.api.libs.json.Reads
import scala.util.Try
import models.Util
import models._
import ApartmentsFilters._
import scala.collection.immutable.Map

/**
 * Static List/Seq of request parameters
 */
object ApartmentsFilters {
  val SUF_GT = "Gt"
  val SUF_LT = "Lt"
  val SUF_GTE = "Gte"
  val SUF_LTE = "Lte"

  val AD_TYPE_P = "adType";
  val PERIOD_P = "period";
  val PRICE = "price";
  val ROOMS = "rooms";
  val DISTRICT_P = "district";

  val pList = List(
    new Parameter(ROOMS + SUF_GTE, 0),
    new Parameter(ROOMS + SUF_LTE, 0),
    new Parameter(PRICE + SUF_GTE, 0),
    new Parameter(PRICE + SUF_LTE, 0),
    new Parameter(DISTRICT_P, 0),
    new Parameter(PERIOD_P, 0),
    new Parameter(AD_TYPE_P, 0),
    new Parameter("cityId", 0),
    new ParameterWithName("fu", false, "Мебель"),
    new ParameterWithName("ai", false, "Кондиционер"),
    new ParameterWithName("fr", false, "Камин"),
    new ParameterWithName("ca", false, "Кабельное ТВ"),
    new ParameterWithName("wa", false, "Стиральная машинка"),
    new ParameterWithName("fi", false, "Холодильник"),
    new ParameterWithName("it", false, "Интернет"),
    new ParameterWithName("bo", false, "Бойлер"),
    new ParameterWithName("ne", false, "Рядомпарковка"))

  val additionFilterList: List[ParameterWithName] = pList.filter(p => p.isInstanceOf[ParameterWithName])
    .sortBy(_.asInstanceOf[ParameterWithName].label).asInstanceOf[List[ParameterWithName]]

  val pMap: Map[String, Parameter] = pList.map { p => (p.name, p) } toMap
}

/**
 *
 */
class ApartmentsP(val cityName: String,
  val cityId: Long,
  override val page: Int,
  requestParameters: Map[String, Seq[String]]) extends HttpParametersStore(requestParameters, pMap) with PageQueryGenerator {

  override def toQueryString() = {
    "/ua/" + cityName + "/" + page + Util.buildEncodedQueryString(paramMap.toList)
  }

  override def toQueryString(page: Int) = {
    "/ua/" + cityName + "/" + page + Util.buildEncodedQueryString(paramMap.toList)
  }

  override def toQueryString(page: Int, list: List[(String, AnyVal)]) = {
    "/ua/" + cityName + "/" + page + Util.buildEncodedQueryString(list)
  }

  override def toString() = "ApartmentP: {%s}".format(toQueryString)
}

object ApartmentsP {
  def read(cityName: String, cityId: Long, page: Int, requestParameters: Map[String, Seq[String]]): ApartmentsP = {
    new ApartmentsP(cityName, cityId, page, requestParameters)
  }
}

