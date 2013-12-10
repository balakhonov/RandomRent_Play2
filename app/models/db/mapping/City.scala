package models.db.mapping

class City(
  val id: Long,
  var name: String,
  var countryId: Long,
  var provinceId: Long,
  var lat: Double,
  var lon: Double) extends BaseEntity {

  def this() = this(0, "", 0, 0, 0, 0)

  override def toString() = {
    "City:{id:%s,name:%s,countryId:%s,provinceId:%s,lat:%s,lon:%s}"
      .format(id, name, countryId, provinceId, lat, lon)
  }
}