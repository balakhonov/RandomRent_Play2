package controllers

import controllers.Actions.Logging
import models.db.dao.CityDao
import models.json.formatters.JsonCityFormatter
import play.api.Logger
import play.api.libs.json.Json.toJson
import play.api.mvc.Action
import play.api.mvc.Controller
import models.json.formatters.JsonDistrictFormatter
import models.db.dao.DistrictDao

object DistrictController extends Controller {
  private val LOG = Logger(getClass)
  private implicit val formatter = JsonDistrictFormatter

  /**
   *
   */
  def get(districtId: Int) = {
    Logging {
      Action { implicit request =>
        {
          var entry = DistrictDao.get(districtId)
          Ok(toJson(entry))
        }
      }
    }
  }

  /**
   *
   */
  def getByCity(cityId: Int) = {
    Logging {
      Action { implicit request =>
        {
          var districts = DistrictDao.getByCity(cityId)
          Ok(toJson(districts))
        }
      }
    }
  }
}