package controllers

import scala.concurrent.Future
import controllers.Actions.ApartmentAction
import controllers.Actions.ApartmentActionBuilder
import controllers.Actions.JsonAction
import controllers.Actions.Logging
import controllers.Actions.LoggingAction
import controllers.Actions.Session
import controllers.mapping.ApartmentFilters
import controllers.mapping.ApartmentsP
import models.Pagination
import models.Util
import models.db.dao.ApartmentDao
import models.db.dao.CityDao
import models.db.mapping.Apartment
import models.json.formatters.JsonApartmentFormatter
import play.api.Logger
import play.api.libs.json.Json.toJson
import play.api.mvc.Action
import play.api.mvc.Controller
import play.filters.csrf.CSRFCheck
import models.db.dao.ProvinceDao
import models.json.formatters.JsonProvinceFormatter

object ApartmentController extends Controller {
  private val LOG = Logger(getClass)

  implicit var apartmentFormatter = JsonApartmentFormatter
  var counter: Int = 0;
  var UA_ID = 1

  /**
   *
   */
  private val ITEMS_PER_PAGE = 5

  /**
   * Count of visible buttons on pagination
   */
  private val VISIBLE_BUTTONS_COUNT = 2

  /**
   *
   */
  def get(id: Int) = {
    Logging {
      ApartmentActionBuilder(id) { request =>
        {
          Ok(views.html.user.apartments.apartment(request.apartment)).withSession((Util.USER_ID, "1"))
        }
      }
    }
  }

  /**
   *
   */
  def addApartment() =
    Logging {
      Action { implicit request =>
        {
          implicit var formatter = JsonProvinceFormatter
          var provinces = toJson(ProvinceDao.getAll)
          LOG.error(provinces.toString)

          Ok(views.html.user.apartments.add_apartment(provinces))
        }
      }
    }

  /**
   *
   */
  def create(archived: Boolean, moderated: Boolean) = CSRFCheck {
    Session {
      JsonAction[Apartment](JsonApartmentFormatter) { implicit request =>
        {
          var apartment = request.item

          // validate

          ApartmentDao.save(apartment)
          Ok(toJson(apartment))
        }
      }
    }
  }

  /**
   *
   */
  def update(id: Int) = CSRFCheck {
    Session {
      ApartmentAction(id, parse.json) { apartment =>
        JsonAction[Apartment](JsonApartmentFormatter) { request =>
          {
            var item = request.item

            // validate

            // DAO update

            Ok(toJson(request.item))
          }
        }
      }
    }
  }

  def delete(id: Int) = CSRFCheck {
    Session {
      LoggingAction(parse.json) { request =>
        {
          LOG.debug("delete: " + request.body);
          Ok
        }
      }
    }
  }

  def show(cityName: String, page: Int) =
    Logging {
      Action { implicit request =>
        {
          val city = CityDao.getCity(UA_ID, cityName)
          val offset = ITEMS_PER_PAGE * (page - 1)

          if (city == null || offset < 0)
            Ok("Page not found")
          else {
            implicit val ap = ApartmentsP.read(cityName, city.id, page, request.queryString)
            LOG.debug("queryString:{page:%s,cityName:%s,}".format(page, cityName, ap.toQueryString))

            val totalApartments = ApartmentDao.getApartmentsCount(city.id)
            var apartments = ApartmentDao.getApartments(city.id, ITEMS_PER_PAGE, offset)
            var totalPages = Math.ceil(totalApartments.toFloat / ITEMS_PER_PAGE.toFloat).toInt

            render {
              case Accepts.Html() => {
                val pagination = Pagination.getPagination(page, totalPages, VISIBLE_BUTTONS_COUNT)
                Ok(views.html.user.apartments.apartments(apartments, page, totalPages, pagination, new ApartmentFilters(ap)))
              }
              case Accepts.Json() => {
                val jsonList = toJson(apartments.map(toJson(_)))
                Ok(jsonList)
              }
            }
          }
        }
      }
    }
}