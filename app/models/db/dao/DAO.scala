package models.db.dao

import org.squeryl.Table
import org.squeryl.PrimitiveTypeMode.__thisDsl
import org.squeryl.PrimitiveTypeMode.from
import org.squeryl.PrimitiveTypeMode.update
import org.squeryl.PrimitiveTypeMode.long2ScalarLong
import org.squeryl.PrimitiveTypeMode.orderByArg2OrderByExpression
import org.squeryl.PrimitiveTypeMode.transaction
import org.squeryl.PrimitiveTypeMode.typedExpression2OrderByArg
import org.squeryl.PrimitiveTypeMode.where
import controllers.mapping.HttpParametersStore.makeRestrics
import models.db.mapping.BaseEntity
import scala.util.Try
import controllers.mapping.HttpParametersStore
import org.squeryl.PrimitiveTypeMode

class DAO[I <: BaseEntity](_table: Table[I]) {
  val table = _table

  def get(id: Long): I = {
    transaction {
      Try(from(table)(s => where(s.id === id) select (s)).single).getOrElse(null).asInstanceOf[I]
    }
  }

  def save(item: I): I = {
    transaction {
      table.insert(item)
    }
  }

  def update(item: I) = {
    transaction {
      table.update(item)
    }
  }

  //TODO make settersfrom parameters Map
  //  def updateAll()(implicit ps: HttpParametersStore): Int = {
  //    transaction {
  //      PrimitiveTypeMode.update(table)(s =>
  //        where(makeRestrics())
  //          set (s.title := "The Watermelon Man", s.year := s.year.~ + 1))
  //
  //    }
  //  }

  def delete(id: Long): Int = {
    transaction {
      table.deleteWhere(item => item.id === id)
    }
  }
}