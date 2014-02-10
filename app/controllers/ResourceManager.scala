package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import play.api.Logger
import play.api.Play
import play.api.Play.current
import play.api.libs.iteratee.Enumerator
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.ResponseHeader
import play.api.mvc.SimpleResult
import play.utils.UriEncoding

object ResourceManager extends Controller {
  private val LOG = Logger(getClass)

  /**
   *
   */
  def getResourse(path: String, fileName: String) = Action {
    val decodedFile = UriEncoding.decodePath(fileName, "utf-8")
    val absolutePath = path + "/" + decodedFile
    LOG.trace("Try to load: " + absolutePath)

    val resource = Play.resource(absolutePath)
    LOG.trace("Resource: " + resource)

    val resourcePath = resource.get.getFile()
    LOG.trace("ResourcePath: " + resourcePath)

    val resourceFile = new java.io.File(resourcePath)
    LOG.trace("resourceFile exists: " + resourceFile.exists())
    LOG.trace("resourceFile isDirectory: " + resourceFile.isDirectory())

    if (!resourceFile.exists() || resourceFile.isDirectory()) {
      NotFound
    } else {
      val fileContent: Enumerator[Array[Byte]] = Enumerator.fromFile(resourceFile, 1024 * 8)

      SimpleResult(
        header = ResponseHeader(200),
        body = fileContent).withHeaders("Cache-Control" -> "max-age=3600")
    }
  }
}