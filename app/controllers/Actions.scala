package controllers

import java.net.URLDecoder

import scala.concurrent.Future

import models.Util
import models.db.dao.ApartmentDao
import models.db.dao.CityDao
import models.db.mapping.Apartment
import models.db.mapping.City
import play.api.Logger
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.mvc.Action
import play.api.mvc.ActionBuilder
import play.api.mvc.BodyParser
import play.api.mvc.BodyParsers.parse
import play.api.mvc.Request
import play.api.mvc.Result
import play.api.mvc.Results
import play.api.mvc.SimpleResult
import play.api.mvc.WrappedRequest

object Actions extends Results {
  private val LOG = Logger(getClass)

  protected case class RequestWithApartment[A](val apartment: Apartment, request: Request[A]) extends WrappedRequest[A](request)
  protected case class RequestWithCity[A](val city: City, request: Request[A]) extends WrappedRequest[A](request)
  protected case class RequestWithItem[A](val item: A, request: Request[JsValue])

  object JsonAction {
    def apply[I](formatter: Reads[I])(block: RequestWithItem[I] => Result) = {
      LoggingAction(parse.json)(request => fromJson[I](block, request, formatter))
    }
  }

  private def fromJson[I](block: RequestWithItem[I] => Result, request: Request[JsValue], formatter: Reads[I]): Result = {
    var json: JsValue = request.body
    var jsResult = Json.fromJson[I](json)(formatter)
    var item = jsResult.get
    block(new RequestWithItem[I](item, request))
  }

  case class Session[A](action: Action[A]) extends Action[A] {
    def apply(request: Request[A]): Future[SimpleResult] = {
      request.session.get(Util.USER_ID).map { user =>
        action(request)
      }.getOrElse {
        Future.successful(Unauthorized("You are not authorized!"))
      }
    }

    lazy val parser = action.parser
  }

  case class ApartmentAction[A](id: Int, p: BodyParser[A])(action: (Apartment) => Action[A]) extends Action[A] {
    //TODO validate ID
    lazy val parser = p

    def apply(request: Request[A]): Future[SimpleResult] = {
      var ap = ApartmentDao.get(id)
      var act = action(ap)
      act(request)
    }
  }

  object ApartmentActionBuilder {
    def apply(id: Int) = {
      new ApartmentActionBuilder(id)
    }

    class ApartmentActionBuilder(id: Int) extends ActionBuilder[RequestWithApartment] {
      def invokeBlock[A](request: Request[A], block: (RequestWithApartment[A]) => Future[SimpleResult]) = {
        block(new RequestWithApartment(ApartmentDao.get(id), request))
      }
    }
  }

  object CityActionBuilder {
    def apply(id: Int) = {
      new CityActionBuilder(id)
    }

    class CityActionBuilder(id: Int) extends ActionBuilder[RequestWithCity] {
      def invokeBlock[A](request: Request[A], block: (RequestWithCity[A]) => Future[SimpleResult]) = {
        block(new RequestWithCity(CityDao.get(id), request))
      }
    }
  }

  object LoggingAction extends ActionBuilder[Request] {
    override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[SimpleResult]) = {
      block(request)
    }

    override def composeAction[A](action: Action[A]) = new Logging(action)
  }

  case class Logging[A](action: Action[A]) extends Action[A] {
    def apply(r: Request[A]): Future[SimpleResult] = {
      LOG.debug("[%s] %s %s [session:%s] %nparameters:{%s}"
        .format(r.remoteAddress, r.method, URLDecoder.decode(r.path, "UTF-8"), r.session.get(Util.USER_ID), r.queryString))
      action(r)
    }
    lazy val parser = action.parser
  }
}