package models.db.mapping

class Apartment(
  var cityId: Long,
  var street: String,
  var rooms: Int,
  var adType: Int,
  var period: Int,
  var rentById: Int,
  var price: Int,
  var title: String,
  var description: String,
  var mobileNumber: String,
  var email: String,
  var postedDate: Long,
  var fu: Boolean,
  var ai: Boolean,
  var fr: Boolean,
  var ca: Boolean,
  var wa: Boolean,
  var fi: Boolean,
  var it: Boolean,
  var bo: Boolean,
  var ne: Boolean,
  var district: Int,
  var archived: Boolean,
  var moderated: Boolean,
  var pLat: Double,
  var pLong: Double) extends BaseEntity {

  val id: Long = 0L

  def this() = this(0, "", 0, 0, 0, 0, 0, "", "", "", "", 0, false, false, false, false, false, false, false, false, false, 0, false, false, 0, 0)

  override def toString = {
    "id = " + id + "street:" + street
  }
}

object Apartment {

  def fromParser(
    id: Int,
    cityId: Int,
    street: String,
    rooms: Int,
    adType: Int,
    period: Int,
    rentById: Int,
    price: Int,
    title: String,
    description: String,
    mobileNumber: String,
    email: String,
    postedDate: Long,
    fu: Boolean,
    ai: Boolean,
    fr: Boolean,
    ca: Boolean,
    wa: Boolean,
    fi: Boolean,
    it: Boolean,
    bo: Boolean,
    ne: Boolean,
    district: Int,
    archived: Boolean,
    moderated: Boolean,
    pLat: Double,
    pLong: Double): Apartment = {
    new Apartment(
      cityId,
      street: String,
      rooms,
      adType,
      period,
      rentById,
      price,
      title,
      description,
      mobileNumber,
      email,
      postedDate,
      fu,
      ai,
      fr,
      ca,
      wa,
      fi,
      it,
      bo,
      ne,
      district,
      archived,
      moderated,
      pLat,
      pLong)
  }
}