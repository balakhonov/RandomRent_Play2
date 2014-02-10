package controllers

import controllers.Actions.Logging
import play.api.Logger
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.i18n.Lang
import controllers.mapping.MetaHeaders

object Application extends Controller {

  private val LOG = Logger(getClass)
  //  private implicit var lang = new Lang("en")

  /**
   *
   */
  //  def index(i18n: String) = Action { request =>
  //    //    request.acceptLanguages()
  //    implicit val lang = new Lang(i18n)
  //    Ok(views.html.index("Your new application is ready."))
  //  }

  /**
   *
   */
  def index = Action { request =>
    for (el <- request.acceptLanguages) {
      LOG.error(el.language)
    }

    implicit val lang = new Lang("ru")
    Ok(views.html.index())
  }

  /**
   *
   */
  def indexI18n(i18n: String) = Action { request =>
    implicit val lang = new Lang(i18n)
    Ok(views.html.index())
  }

  /**
   *
   */
  def add() = {
    Logging {
      Action {
        Ok(views.html.user.add())
      }
    }
  }

  val echo = Action { request =>
    Ok("Got request [" + request + "]")
  }

  def ua(city: String) = Action {
    Ok("City: " + city)
  }
}