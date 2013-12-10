package models.db.dao

import play.api.Logger
import models.db.Schema
import org.squeryl.PrimitiveTypeMode.__thisDsl
import org.squeryl.PrimitiveTypeMode.from
import org.squeryl.PrimitiveTypeMode.string2ScalarString
import org.squeryl.PrimitiveTypeMode.long2ScalarLong
import org.squeryl.PrimitiveTypeMode.int2ScalarInt
import org.squeryl.PrimitiveTypeMode.orderByArg2OrderByExpression
import org.squeryl.PrimitiveTypeMode.transaction
import org.squeryl.PrimitiveTypeMode.typedExpression2OrderByArg
import org.squeryl.PrimitiveTypeMode.where
import models.db.mapping.City
import scala.util.Try
import org.squeryl.dsl.StringExpression

object CityDao extends DAO(Schema.cities) {
  private val LOG = Logger(getClass)

  /**
   *
   */
  def getCity(countryId: Long, name: String): City = {
    if (countryId < 1) {
      throw new IllegalArgumentException("Country ID(" + countryId + ") should not be < 1")
    }
    if (name == null || name.isEmpty) {
      throw new IllegalArgumentException("City name should not be null or empty")
    }

    transaction {
      Try(from(table)(s =>
        where(s.name like name and s.countryId === countryId)
          select (s)).single)
        .getOrElse(null).asInstanceOf[City]
    }
  }

  /**
   *
   */
  def getByProvince(provinceId: Long): List[City] = {
    if (provinceId < 1) {
      throw new IllegalArgumentException("Province ID(%s) should not be < 1".format(provinceId))
    }

    transaction {
      from(table)(s =>
        where(s.provinceId === provinceId)
          select (s)).toList
    }
  }

  /**
   *
   */
  def getByCountry(countryId: Long): List[City] = {
    if (countryId < 1) {
      throw new IllegalArgumentException("Country ID(%s) should not be < 1".format(countryId))
    }

    transaction {
      from(table)(s =>
        where(s.countryId === countryId)
          select (s)).toList
    }
  }
}