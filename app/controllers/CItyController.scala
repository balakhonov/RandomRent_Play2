package controllers

import controllers.Actions.Logging
import models.db.dao.CityDao
import models.json.formatters.JsonCityFormatter
import play.api.Logger
import play.api.libs.json.Json.toJson
import play.api.mvc.Action
import play.api.mvc.Controller

object CityController extends Controller {
  private val LOG = Logger(getClass)
  private implicit val formatter = JsonCityFormatter

  /**
   *
   */
  def get(cityId: Int) = {
    Logging {
      Action { implicit request =>
        {
          var city = CityDao.get(cityId)
          Ok(toJson(city))
        }
      }
    }
  }

  /**
   *
   */
  def getByProvince(provinceId: Int) = {
    Logging {
      Action { implicit request =>
        {
          var cities = CityDao.getByProvince(provinceId)
          Ok(toJson(cities))
        }
      }
    }
  }

  /**
   *
   */
  def getByCountry(countryId: Int) = {
    Logging {
      Action { implicit request =>
        {
          var cities = CityDao.getByCountry(countryId)
          Ok(toJson(cities))
        }
      }
    }
  }
}