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

object JsonProvinceFormatter extends Format[Province] {

  override def writes(ap: Province): JsValue = {
    toJson(Map(
      "id" -> toJson(ap.id),
      "countryId" -> toJson(ap.countryId),
      "name" -> toJson(ap.name)))
  }

  override def reads(j: JsValue): JsResult[Province] = {
    JsSuccess[Province](
      new Province(
        (j \ "id").as[Option[Long]].getOrElse(0),
        (j \ "name").as[Option[String]].getOrElse(""),
        (j \ "countryId").as[Option[Long]].getOrElse(0L)))
  }
}