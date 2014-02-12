package models.db.dao

import play.api.Logger
import models.db.Schema
import org.squeryl.PrimitiveTypeMode.from
import org.squeryl.PrimitiveTypeMode.long2ScalarLong
import org.squeryl.PrimitiveTypeMode.orderByArg2OrderByExpression
import org.squeryl.PrimitiveTypeMode.transaction
import org.squeryl.PrimitiveTypeMode.typedExpression2OrderByArg
import org.squeryl.PrimitiveTypeMode.where
import models.db.mapping.ApartmentImage

object ApartmentImageDao extends DAO(Schema.apartmentImages) {
  private val LOG = Logger(getClass)

  def getApartmentImages(apartmentId: Long): List[ApartmentImage] = {

      transaction {
        val list = from(table)(ap =>
          where(ap.apartmentId === apartmentId)
            select ap
            orderBy (ap.id desc)).toList
        LOG.debug("Apartment images list: " + list)
        list
      }
  }
}