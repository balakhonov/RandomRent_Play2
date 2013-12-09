package controllers.mapping

import scala.util.Try
import controllers.mapping.HttpParametersStore.parse
import org.squeryl.PrimitiveTypeMode.int2ScalarInt
import org.squeryl.dsl.ast.BinaryOperatorNodeLogicalBoolean
import org.squeryl.dsl.ast.DummyExpressionHolder
import org.squeryl.dsl.ast.LogicalBoolean
import scala.collection.immutable.Map

class Parameter(var name: String, var default: AnyVal)
class ParameterWithName(name: String,
  default: AnyVal,
  var label: String) extends Parameter(name, default)

/**
 * This class gets request parameters, checks them with expected parameters
 * and saves same parameters with requested values to new map
 *
 * @param map - expected parameters
 * @param rp - requested parameters
 */
class HttpParametersStore(_paramMap: Map[String, AnyVal]) {
  println("_paramMap:" + _paramMap)
  var paramMap: Map[String, AnyVal] = _paramMap

  def this(rp: Map[String, Seq[String]], map: Map[String, Parameter]) = {
    this{
      (for (p1 <- map) yield rp.get(p1._1).map(p1._1 -> parse(_, p1._2.default)).getOrElse(("", 0)))
        .filter(!_._1.equals(""))
    }
  }

  override def toString() = paramMap.map(e => "%s: %s".format(e._1, e._2)).mkString(", ")
}

/**
 *
 */
object HttpParametersStore {

  /**
   *
   */
  def makeRestrics()(implicit pp: HttpParametersStore): LogicalBoolean = {
    pp.paramMap.map { p => getLogicBoolean(p._1, p._2) }.foldLeft(1 gte 1)((a, b) => a.and(b))
  }

  /**
   *
   */
  private def getLogicBoolean(left: String, right: AnyVal): LogicalBoolean = {
    var field = left
    var operation = left match {
      case x if x endsWith "Gt" => { field = x.dropRight(2); ">" }
      case x if x endsWith "Gte" => { field = x.dropRight(3); ">=" }
      case x if x endsWith "Lt" => { field = x.dropRight(2); "<" }
      case x if x endsWith "Lte" => { field = x.dropRight(3); "<=" }
      case _ => "="
    }

    new BinaryOperatorNodeLogicalBoolean(new DummyExpressionHolder(field), new DummyExpressionHolder(right.toString), operation)
  }

  def parseInt(p: Map[String, Seq[String]], key: String, default: Int): Int = {
    Try(p.get(key).get(0).toInt).getOrElse(default)
  }

  def parseString(p: Map[String, Seq[String]], key: String, default: String): String = {
    Try(p.get(key).get(0).toString).getOrElse(default)
  }

  def parseBoolean(p: Map[String, Seq[String]], key: String, default: Boolean): Boolean = {
    Try(p.get(key).get(0).toBoolean).getOrElse(default)
  }

  def parseFloat(p: Map[String, Seq[String]], key: String, default: Float): Float = {
    Try(p.get(key).get(0).toFloat).getOrElse(default)
  }

  def parseInt(v: Seq[String], default: Int): Int = {
    Try(v(0).toInt).getOrElse(default)
  }

  def parseString(v: Seq[String], default: String): String = {
    Try(v(0).toString).getOrElse(default)
  }

  def parseBoolean(v: Seq[String], default: Boolean): Boolean = {
    Try(v(0).toBoolean).getOrElse(default)
  }

  def parseFloat(v: Seq[String], default: Float): Float = {
    Try(v(0).toFloat).getOrElse(default)
  }

  def parse[I](p: Map[String, Seq[String]], key: String, default: I): I = {
    default match {
      case _: Int => parseInt(p, key, default.asInstanceOf[Int]).asInstanceOf[I]
      case _: Float => parseFloat(p, key, default.asInstanceOf[Float]).asInstanceOf[I]
      case _: Boolean => parseBoolean(p, key, default.asInstanceOf[Boolean]).asInstanceOf[I]
      case _: String => parseString(p, key, default.asInstanceOf[String]).asInstanceOf[I]
      case _ => throw new IllegalStateException("Unknownclass type")
    }
  }

  def parse[I](v: Seq[String], default: I): I = {
    default match {
      case _: Int => parseInt(v, default.asInstanceOf[Int]).asInstanceOf[I]
      case _: Float => parseFloat(v, default.asInstanceOf[Float]).asInstanceOf[I]
      case _: Boolean => parseBoolean(v, default.asInstanceOf[Boolean]).asInstanceOf[I]
      case _: String => parseString(v, default.asInstanceOf[String]).asInstanceOf[I]
      case _ => throw new IllegalStateException("Unknownclass type")
    }
  }
}