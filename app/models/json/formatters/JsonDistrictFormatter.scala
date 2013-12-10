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
import models.db.mapping.District

object JsonDistrictFormatter extends Format[District] {

  override def writes(ap: District): JsValue = {
    toJson(Map(
      "id" -> toJson(ap.id),
      "cityId" -> toJson(ap.cityId),
      "name" -> toJson(ap.name)))
  }

  override def reads(j: JsValue): JsResult[District] = {
    JsSuccess[District](
      new District(
        (j \ "id").as[Option[Long]].getOrElse(0),
        (j \ "name").as[Option[String]].getOrElse(""),
        (j \ "cityId").as[Option[Long]].getOrElse(0L)))
  }
}