package models.db.mapping

class Province(var name: String, var countryId: Long) extends BaseEntity {

  val id: Long = 0L

  def this() = this("", 0)

  override def toString() = {
    "District:{id:%s,name:%s,countryId:%s}"
      .format(id, name, countryId)
  }
}