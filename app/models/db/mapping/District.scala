package models.db.mapping

class District(var name: String, var cityId: Long) extends BaseEntity {

  val id: Long = 0L

  def this() = this("", 0)

  override def toString() = {
    "District:{id:%s,name:%s,cityId:%s}"
      .format(id, name, cityId)
  }
}