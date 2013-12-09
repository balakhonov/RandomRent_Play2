package models.db.dao

import play.api.Logger
import models.db.Schema
import org.squeryl.PrimitiveTypeMode.__thisDsl
import org.squeryl.PrimitiveTypeMode.from
import org.squeryl.PrimitiveTypeMode.string2ScalarString
import org.squeryl.PrimitiveTypeMode.long2ScalarLong
import org.squeryl.PrimitiveTypeMode.orderByArg2OrderByExpression
import org.squeryl.PrimitiveTypeMode.transaction
import org.squeryl.PrimitiveTypeMode.typedExpression2OrderByArg
import org.squeryl.PrimitiveTypeMode.where
import models.db.mapping.City
import scala.util.Try
import org.squeryl.dsl.StringExpression
import models.db.mapping.District

object DistrictDao extends DAO(Schema.districts) {
  private val LOG = Logger(getClass)

  def getByCity(cityId: Long): List[District] = {
    LOG.debug("cityId: " + cityId)
    if (cityId < 1) {
      throw new IllegalArgumentException("City ID(%s) should not be < 1".format(cityId))
    }

    transaction {
      from(table)(s => where(s.cityId === cityId) select (s)).toList
    }
  }
}