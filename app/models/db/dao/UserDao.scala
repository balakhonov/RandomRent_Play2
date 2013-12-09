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

object UserDao extends DAO(Schema.users) {
  private val LOG = Logger(getClass)

  /**
   *
   */
  def getByEmail(email: String): City = {
    if (email == null || email.isEmpty) {
      throw new IllegalArgumentException("Email should not be null or empty")
    }

    transaction {
      Try(from(table)(s =>
        where(s.email === (email))
          select (s)).single)
        .getOrElse(null).asInstanceOf[City]
    }
  }
}