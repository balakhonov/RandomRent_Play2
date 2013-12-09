package controllers

import controllers.Actions.Logging
import play.api.mvc.Action
import play.api.mvc.Controller

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
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