package models.db

import org.squeryl._
import org.squeryl.PrimitiveTypeMode._
import models.db.mapping.Apartment
import org.squeryl.adapters.MySQLAdapter
import play.Logger
import models.db.mapping.City
import models.db.mapping.User
import models.db.mapping.Country
import models.db.mapping.District
import models.db.mapping.Province
import models.db.mapping.ApartmentImage

object Schema extends Schema {
  val users = table[User]("users")
  val countries = table[Country]("countries")
  val provinces = table[Province]("provinces")
  val cities = table[City]("cities")
  val districts = table[District]("districts")
  val apartments = table[Apartment]("apartment")
  val apartmentImages = table[ApartmentImage]("apartment_images")

  on(users)(user => declare(
    user.id is (autoIncremented),
    user.email is (unique)))

  on(countries)(entry => declare(entry.id is (autoIncremented)))

  on(provinces)(entry => declare(entry.id is (autoIncremented)))

  on(cities)(entry => declare(entry.id is (autoIncremented)))

  on(districts)(entry => declare(entry.id is (autoIncremented)))

  on(apartments)(entry => declare(entry.id is (autoIncremented)))

  on(apartmentImages)(entry => declare(entry.id is (autoIncremented)))

  //  val provincesToCountries = oneToManyRelation(countries, provinces).via((c, p) => c.id === p.countryId)
  //  val citiesToProvinces = oneToManyRelation(provinces, cities).via((p, c) => p.id === c.provinceId)
  //  val districtsToCities = oneToManyRelation(cities, districts).via((c, d) => c.id === d.cityId)
  //  val apartmentsToCities = oneToManyRelation(cities, apartments).via((c, a) => c.id === a.cityId)
  //  val apartmentImagesToApartments = oneToManyRelation(apartments, apartmentImages).via((a, ap) => a.id === ap.apartmentId)

  override def drop = {
    Session.cleanupResources
    super.drop
  }

}
