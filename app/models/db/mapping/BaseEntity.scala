package models.db.mapping

import java.sql.Timestamp
import org.squeryl.KeyedEntity

trait BaseEntity extends KeyedEntity[Long] {

  val id: Long
  var lastModified = new Timestamp(System.currentTimeMillis)

}