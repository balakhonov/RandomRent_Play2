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
      "email" -> toJson(ap.email),
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
      "moderated" -> toJson(ap.moderated),
      "pLat" -> toJson(ap.pLat),
      "pLong" -> toJson(ap.pLong)))
  }

  override def reads(j: JsValue): JsResult[Apartment] = {
    JsSuccess[Apartment](
      Apartment.fromParser(
        id = (j \ "id").asOpt[Int].getOrElse(0),
        cityId = (j \ "cityId").asOpt[Int].getOrElse(0),
        street = (j \ "street").asOpt[String].getOrElse(""),
        rooms = (j \ "rooms").asOpt[Int].getOrElse(0),
        adType = (j \ "adType").asOpt[Int].getOrElse(0),
        period = (j \ "period").asOpt[Int].getOrElse(0),
        rentById = (j \ "rentById").asOpt[Int].getOrElse(0),
        price = (j \ "price").asOpt[Int].getOrElse(0),
        title = (j \ "title").asOpt[String].getOrElse(""),
        description = (j \ "description").asOpt[String].getOrElse(""),
        mobileNumber = (j \ "mobileNumber").asOpt[String].getOrElse(""),
        email = (j \ "email").asOpt[String].getOrElse(""),
        postedDate = (j \ "postedDate").asOpt[Long].getOrElse(new Date().getTime()),
        fu = (j \ "fu").asOpt[Boolean].getOrElse(false),
        ai = (j \ "ai").asOpt[Boolean].getOrElse(false),
        fr = (j \ "fr").asOpt[Boolean].getOrElse(false),
        ca = (j \ "ca").asOpt[Boolean].getOrElse(false),
        wa = (j \ "wa").asOpt[Boolean].getOrElse(false),
        fi = (j \ "fi").asOpt[Boolean].getOrElse(false),
        it = (j \ "it").asOpt[Boolean].getOrElse(false),
        bo = (j \ "bo").asOpt[Boolean].getOrElse(false),
        ne = (j \ "ne").asOpt[Boolean].getOrElse(false),
        district = (j \ "district").asOpt[Int].getOrElse(0),
        archived = (j \ "archived").asOpt[Boolean].getOrElse(false),
        moderated = (j \ "moderated").asOpt[Boolean].getOrElse(false),
        pLat = (j \ "pLat").asOpt[Double].getOrElse(0),
        pLong = (j \ "pLong").asOpt[Double].getOrElse(0)))
  }
}