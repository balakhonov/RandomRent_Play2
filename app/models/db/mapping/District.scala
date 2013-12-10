package models.db.mapping

class District(val id: Long,
  var name: String,
  var cityId: Long) extends BaseEntity {

  def this() = this(0, "", 0)

  override def toString() = {
    "District:{id:%s,name:%s,cityId:%s}"
      .format(id, name, cityId)
  }
}