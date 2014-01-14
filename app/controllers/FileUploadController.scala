package controllers

import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import models.db.mapping.ApartmentImage
import models.db.dao.ApartmentImageDao
import java.security.MessageDigest
import play.filters.csrf.CSRFCheck
import play.api.mvc.Controller
import play.api.Logger
import controllers.Actions.Session
import controllers.Actions.LoggingAction
import java.util.Date
import java.io.InputStream
import java.awt.Color
import java.awt.AlphaComposite
import play.api.mvc.MultipartFormData.FilePart
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.Json.toJson
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import java.io.FileInputStream
import play.api.mvc.Action
import play.api.mvc.Request
import play.api.mvc.SimpleResult
import play.api.mvc.MultipartFormData
import play.api.mvc.AnyContent
import play.api.mvc.AnyContentAsMultipartFormData

object FileUploadController extends Controller {
  private val LOG = Logger(getClass)
  val SUFFIX_MAX = "-max.jpg"
  val SUFFIX_THUMB_150 = "-thumb-150x150.jpg"
  val SUFFIX_THUMB_200 = "-thumb-200x200.jpg"

  val IMAGES_MAP = new ConcurrentHashMap[String, scala.collection.mutable.Set[String]]()
  val AVAILABLE_CONTENT_TYPES = List("image/jpeg", "image/png")
  val AVAILABLE_EXTENSIONS = List("jpg", "jpeg", "png")

  val allowCrossDomain = true

  val ROOT_IMAGES_DIR = "/home/yuri/RandomRent/images/"

  // Try to make directories if not exists
  val dir = new File(ROOT_IMAGES_DIR);
  if (!dir.exists()) {
    dir.mkdirs();
  }

  /**
   *
   */
  def upload() =
    CSRFCheck {
      LOG.debug("upload1: ")
      Action { request =>
        LOG.debug("upload2: ")
        try {
          request.body match {
            case body: AnyContent if body.asMultipartFormData.isDefined => {
              LOG.debug("Defined MultipartFormData: " + body.asMultipartFormData.get)
              uploadFile(request, body.asMultipartFormData.get)
            }
            case body: AnyContent if body.asFormUrlEncoded.isDefined => {
              LOG.debug("Defined FormUrlEncoded: " + body.asFormUrlEncoded.get)
              Ok.withHeaders(getCrossDomainHeaders(): _*)
            }
            case body: AnyContent if body.asRaw.isDefined => {
              LOG.debug("Defined Raw: " + body.asRaw)
              Ok.withHeaders(getCrossDomainHeaders(): _*)
            }
            case body: Map[_, _] => {
              LOG.debug("Defined Map: " + body.asInstanceOf[Map[String, Seq[String]]])
              Ok.withHeaders(getCrossDomainHeaders(): _*)
            }
            case _ => {
              throw new IllegalStateException("Unknown body " + request.body)
            }
          }

        } catch {
          case e: ValidationException => {
            LOG.warn(e.getMessage())
            BadRequest(toJson(Map("validation" -> e.getMessage())))
          }
          case e: LimitException => {
            LOG.warn(e.getMessage())
            BadRequest(toJson(Map("limit" -> e.getMessage())))
          }
          case e: IllegalStateException => {
            LOG.warn(e.getMessage())
            BadRequest(toJson(Map("error" -> e.getMessage())))
          }
        }
      }
    }

  /**
   * Delete uploaded file from server and cache
   */
  def delete(fileName: String) = {
    Action { request =>
      try {
        request.session.get("uuid").map { uuid =>
          var imagesList = IMAGES_MAP.get(uuid)
          if (imagesList != null && imagesList.remove(fileName)) {
            // delete from cache
            IMAGES_MAP.put(uuid, imagesList)

            //delete from file system
            new File(getTmpDirPath() + fileName).delete()
            new File(getTmpDirPath() + fileName + SUFFIX_MAX).delete()
            new File(getTmpDirPath() + fileName + SUFFIX_THUMB_200).delete()
            new File(getTmpDirPath() + fileName + SUFFIX_THUMB_150).delete()

            Ok(toJson(Map("" -> true)))
          } else { throw new IllegalStateException("Img(%s) not found".format(fileName)) }
        }.getOrElse {
          throw new IllegalStateException("User haven't uuid")
        }
      } catch {
        case e: IllegalStateException => {
          LOG.warn(e.getMessage())
          BadRequest(e.getMessage())
        }
      }
    }
  }

  /**
   *
   */
  def loadImage(fileName: String) = {
    Action { request =>
      var absolutePath = getTmpDirPath() + fileName;

      var file = new File(absolutePath);
      if (file.exists()) {
        Ok.sendFile(file)
      } else {
        LOG.error("NoContent: " + file.getAbsoluteFile())
        NoContent
      }
    }
  }

  /**
   *
   */
  private def uploadFile(request: Request[AnyContent], body: MultipartFormData[TemporaryFile]) = {
    request.session.get("uuid").map { uuid =>
      //check images limit
      var imagesList = IMAGES_MAP.get(uuid)
      if (imagesList == null) {
        imagesList = scala.collection.mutable.Set[String]()
      }

      var index = imagesList.size + 1 + "";
      if (imagesList.size == 12) {
        throw new LimitException("Превышен лимит")
      } else {
        // load image
        body.file("picture").map { picture =>
          val filename = picture.filename
          val contentType = picture.contentType.get
          var newFileName = toMd5(new Date().getTime + "")

          LOG.debug("filename: " + filename)
          LOG.debug("contentType: " + contentType.toLowerCase())
          LOG.debug("newFileName: " + newFileName)

          if (!AVAILABLE_CONTENT_TYPES.contains(contentType)) {
            throw new ValidationException("Wrong file contentType(%s)".format(contentType))
          }

          var extension = getExtension(filename)
          if (!AVAILABLE_EXTENSIONS.contains(extension)) {
            throw new ValidationException("Wrong file extension(%s)".format(extension))
          }

          // move file to tmp dir
          var file = new File(getTmpDirPath() + newFileName)
          picture.ref.moveTo(file, true)

          //convert
          var fileInputStream = new FileInputStream(file);
          val data = Stream.continually(fileInputStream.read).takeWhile(-1 !=).map(_.toByte).toArray
          convertImage(data, contentType, newFileName, getTmpDirPath())

          imagesList += newFileName
          IMAGES_MAP.put(uuid, imagesList)
          LOG.debug("imagesList: " + imagesList)
          Ok(toJson(Map(
            "files" ->
              List(Map[String, String](
                "url" -> ("/ap-images/" + newFileName + SUFFIX_MAX),
                "thumbnailUrl" -> ("/ap-images/" + newFileName + SUFFIX_THUMB_200),
                "name" -> (newFileName + SUFFIX_MAX),
                "type" -> "image/jpeg",
                "size" -> "0",
                "deleteUrl" -> ("/ap-images/delete/" + newFileName),
                "deleteType" -> "DELETE",
                "index" -> index))))).withHeaders(getCrossDomainHeaders(): _*)

        }.getOrElse { throw new ValidationException("Missing file") }
      }
    }.getOrElse { throw new IllegalStateException("Session not found") }
  }

  private def getCrossDomainHeaders() = {
    var headers: List[(String, String)] = List(("Access-Control-Allow-Credentials" -> "true"),
      ("Access-Control-Allow-Origin" -> "null"), // Need to add the correct domain in here!!
      //                "Access-Control-Allow-Methods" -> "POST, GET",// Only allow POST
      //                "Access-Control-Max-Age" -> "300", // Cache response for 5 minutes
      ("Access-Control-Allow-Headers" -> "Origin, X-Requested-With, Content-Type, Accept"))

    if (allowCrossDomain)
      headers
    else
      List()
  }

  /**
   *
   */
  def getTmpDirPath(): String = {
    var dateTime = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(new Date())
    "/tmp/RandomRent/uploadedImages/%s/".format(dateTime)
  }

  /**
   *
   */
  def toMd5(s: String): String = {
    new java.math.BigInteger(1, MessageDigest.getInstance("MD5").digest(s.getBytes)).toString(16)
  }

  private def convertImage(array: Array[Byte], contentType: String, newFileName: String, dir: String) = {
    var is: InputStream = new ByteArrayInputStream(array)
    var origBufferedImage: BufferedImage = ImageIO.read(is)

    //convert PNG to JPG
    if ("image/png".equalsIgnoreCase(contentType)) {
      //create new blank for JPG
      var newBufferedImage = new BufferedImage(origBufferedImage.getWidth(), origBufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
      newBufferedImage.createGraphics().drawImage(origBufferedImage, 0, 0, Color.WHITE, null);
      origBufferedImage = newBufferedImage;
    }

    var bufferedImage = resize(origBufferedImage, 800, 600)
    ImageIO.write(bufferedImage, "jpg", new File(dir, newFileName + SUFFIX_MAX))

    bufferedImage = resize(origBufferedImage, 200, 200)
    ImageIO.write(bufferedImage, "jpg", new File(dir, newFileName + SUFFIX_THUMB_200))

    bufferedImage = resize(origBufferedImage, 150, 150)
    ImageIO.write(bufferedImage, "jpg", new File(dir, newFileName + SUFFIX_THUMB_150))

    // clear data and references
    is.close()
    is = null
    origBufferedImage.getGraphics().dispose()
    origBufferedImage.flush()
    origBufferedImage = null
    bufferedImage.getGraphics().dispose()
    bufferedImage.flush()
    bufferedImage = null
  }

  def getExtension(fileName: String): String = {
    fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase()
  }

  def resize(originalImage: BufferedImage, maxWidth: Int, maxHeight: Int): BufferedImage = {
    val height = originalImage.getHeight
    val width = originalImage.getWidth

    if (width <= maxWidth && height <= maxHeight)
      originalImage
    else {
      var newWidth = 0
      var newHeight = 0

      if (width > height) {
        val ratio: Double = width.toDouble / maxWidth.toDouble
        newHeight = (height / ratio).intValue
        newWidth = maxWidth
      } else {
        val ratio: Double = height.toDouble / maxHeight.toDouble
        newWidth = (width / ratio).intValue
        newHeight = maxHeight
      }

      val scaledBI = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB)
      val g = scaledBI.createGraphics
      g.setComposite(AlphaComposite.Src)
      g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
      g.dispose
      scaledBI
    }
  }

  class LimitException(s: String) extends Exception(s) {}
  class ValidationException(s: String) extends Exception(s) {}
}