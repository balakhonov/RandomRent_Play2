package models.db.mapping

class Country(var name: String) extends BaseEntity {

  val id: Long = 0L

  def this() = this("")

  override def toString() = {
    "Country:{id:%s,name:%s}"
      .format(id, name)
  }
}