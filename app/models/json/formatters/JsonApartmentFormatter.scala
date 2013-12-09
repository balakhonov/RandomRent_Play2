package models.json.formatters

import java.util.Date

import models.db.mapping.Apartment
import play.api.libs.json.Format
import play.api.libs.json.JsResult
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

object JsonApartmentFormatter extends Format[Apartment] {

  override def writes(ap: Apartment): JsValue = {
    toJson(Map(
      "id" -> toJson(ap.id),
      "cityId" -> toJson(ap.cityId),
      "street" -> toJson(ap.street),
      "rooms" -> toJson(ap.rooms),
      "adType" -> toJson(ap.adType),
      "rentById" -> toJson(ap.rentById),
      "price" -> toJson(ap.price),
      "title" -> toJson(ap.title),
      "description" -> toJson(ap.description),
      "mobileNumber" -> toJson(ap.mobileNumber),
      "postedDate" -> toJson(ap.postedDate),
      "fu" -> toJson(ap.fu),
      "ai" -> toJson(ap.ai),
      "fr" -> toJson(ap.fr),
      "ca" -> toJson(ap.ca),
      "wa" -> toJson(ap.wa),
      "fi" -> toJson(ap.fi),
      "it" -> toJson(ap.it),
      "bo" -> toJson(ap.bo),
      "ne" -> toJson(ap.ne),
      "district" -> toJson(ap.district),
      "archived" -> toJson(ap.archived),
      "moderated" -> toJson(ap.moderated)))
  }

  override def reads(j: JsValue): JsResult[Apartment] = {
    JsSuccess[Apartment](
      Apartment.fromParser(
        id = (j \ "id").as[Option[Int]].getOrElse(0),
        cityId = (j \ "cityId").as[Option[Int]].getOrElse(0),
        street = (j \ "street").as[Option[String]].getOrElse(""),
        rooms = (j \ "rooms").as[Option[Int]].getOrElse(0),
        adType = (j \ "adType").as[Option[Int]].getOrElse(0),
        period = (j \ "period").as[Option[Int]].getOrElse(0),
        rentById = (j \ "rentById").as[Option[Int]].getOrElse(0),
        price = (j \ "price").as[Option[Int]].getOrElse(0),
        title = (j \ "title").as[Option[String]].getOrElse(""),
        description = (j \ "description").as[Option[String]].getOrElse(""),
        mobileNumber = (j \ "mobileNumber").as[Option[String]].getOrElse(""),
        postedDate = (j \ "postedDate").as[Option[Long]].getOrElse(new Date().getTime()),
        fu = (j \ "fu").as[Option[Boolean]].getOrElse(false),
        ai = (j \ "ai").as[Option[Boolean]].getOrElse(false),
        fr = (j \ "fr").as[Option[Boolean]].getOrElse(false),
        ca = (j \ "ca").as[Option[Boolean]].getOrElse(false),
        wa = (j \ "wa").as[Option[Boolean]].getOrElse(false),
        fi = (j \ "fi").as[Option[Boolean]].getOrElse(false),
        it = (j \ "it").as[Option[Boolean]].getOrElse(false),
        bo = (j \ "bo").as[Option[Boolean]].getOrElse(false),
        ne = (j \ "ne").as[Option[Boolean]].getOrElse(false),
        district = (j \ "district").as[Option[Int]].getOrElse(0),
        archived = (j \ "archived").as[Option[Boolean]].getOrElse(false),
        moderated = (j \ "moderated").as[Option[Boolean]].getOrElse(false)))
  }
}