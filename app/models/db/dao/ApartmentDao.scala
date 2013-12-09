package models.db.dao

import org.squeryl.PrimitiveTypeMode.__thisDsl
import org.squeryl.PrimitiveTypeMode.from
import org.squeryl.PrimitiveTypeMode.long2ScalarLong
import org.squeryl.PrimitiveTypeMode.orderByArg2OrderByExpression
import org.squeryl.PrimitiveTypeMode.transaction
import org.squeryl.PrimitiveTypeMode.typedExpression2OrderByArg
import org.squeryl.PrimitiveTypeMode.where
import controllers.mapping.ApartmentsFilters
import controllers.mapping.HttpParametersStore.makeRestrics
import models.Util.measureTime
import models.db.Schema
import models.db.mapping.Apartment
import play.api.Logger
import controllers.mapping.HttpParametersStore

object ApartmentDao extends DAO(Schema.apartments) {
  private val LOG = Logger(getClass)

  /**
   *
   */
  def getApartments(cityId: Long, limit: Int, offset: Int)(implicit mf: HttpParametersStore): List[Apartment] = {
    if (limit < 0)
      throw new IllegalArgumentException("Limit(%s) should not be < 0".format(limit));
    if (offset < 0)
      throw new IllegalArgumentException("Offset(%s) should not be < 0".format(offset));
    if (mf == null)
      throw new IllegalArgumentException("MainFilters should not be null");

    measureTime("Select Apartments list") {
      transaction {
        var list = from(table)((ap) =>
          where(makeRestrics() and ap.cityId === cityId)
            select (ap)
            orderBy (ap.id desc))
          .page(offset, limit).toList
        LOG.debug("Apartments list: " + list)
        list
      }
    }
  }

  /**
   *
   */
  def getApartmentsCount(cityId: Long)(implicit mf: HttpParametersStore): Long = {
    measureTime("Get Apartments count") {
      transaction {
        var count = table.where(ap => makeRestrics() and ap.cityId === cityId).size
        LOG.debug("Apartments count: " + count)
        count
      }
    }
  }
}