package models.json.formatters

import play.api.libs.json.Format
import models.db.mapping.Province
import play.api.libs.json.JsValue
import play.api.libs.json.Format
import play.api.libs.json.JsResult
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import models.db.mapping.Apartment
import models.db.mapping.City

object JsonCityFormatter extends Format[City] {

  override def writes(ap: City): JsValue = {
    toJson(Map(
      "id" -> toJson(ap.id),
      "name" -> toJson(ap.name),
      "countryId" -> toJson(ap.countryId),
      "provinceId" -> toJson(ap.provinceId),
      "lat" -> toJson(ap.lat),
      "lon" -> toJson(ap.lon)))
  }

  override def reads(j: JsValue): JsResult[City] = {
    JsSuccess[City](
      new City(
        (j \ "id").as[Option[Long]].getOrElse(0),
        (j \ "name").as[Option[String]].getOrElse(""),
        (j \ "countryId").as[Option[Long]].getOrElse(0L),
        (j \ "provinceId").as[Option[Long]].getOrElse(0L),
        (j \ "lat").as[Option[Double]].getOrElse(0),
        (j \ "lon").as[Option[Double]].getOrElse(0)))
  }
}