package models.db.mapping

class Province(val id: Long, var name: String, var countryId: Long) extends BaseEntity {

  def this() = this(0, "", 0)

  override def toString() = {
    "{id:%s,name:%s,countryId:%s}"
      .format(id, name, countryId)
  }
}