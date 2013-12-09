package models.db.mapping

class ApartmentImage(var name: String, var apartmentId: Long) extends BaseEntity {

  val id: Long = 0L

  def this() = this("", 0)

  override def toString() = {
    "District:{id:%s,name:%s,apartmentId:%s}"
      .format(id, name, apartmentId)
  }
}