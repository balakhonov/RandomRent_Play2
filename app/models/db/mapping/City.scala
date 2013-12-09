package models.db.mapping

class City(
  var name: String,
  var countryId: Long,
  var provinceId: Int,
  var lat: Double,
  var lon: Double) extends BaseEntity {

  val id: Long = 0L

  def this() = this("", 0, 0, 0, 0)

  override def toString() = {
    "City:{id:%s,name:%s,countryId:%s,provinceId:%s,lat:%s,lon:%s}"
      .format(id, name, countryId, provinceId, lat, lon)
  }
}