package controllers

import controllers.Actions.Logging
import play.api.Logger
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.i18n.Lang
import models.db.dao.CityDao

object Application extends Controller {

  private val LOG = Logger(getClass)

  /**
   *
   */
  def index = indexI18n()

  /**
   *
   */
  def indexI18n(i18n: String = "ru") = Action {
    implicit request =>
      implicit val lang = new Lang(i18n)

      val group = CityDao.getAll().groupBy(_.provinceId)
      val filteredCities = group.map{ entry => (entry._1,entry._2.map(_.name))}

      Ok(views.html.index(filteredCities))
  }

  /**
   *
   */
  def add() = addI18n()

  /**
   *
   * @param i18n
   * @return
   */
  def addI18n(i18n: String = "ru") = {
    Logging {
      Action {
        implicit val lang = new Lang(i18n)
        Ok(views.html.user.add())
      }
    }
  }

  val echo = Action {
    request =>
      Ok("Got request [" + request + "]")
  }

  def ua(city: String) = Action {
    Ok("City: " + city)
  }
}