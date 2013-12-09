package models.db.mapping

class User(var name: String,
  var email: String,
  var password: String) extends BaseEntity {

  val id: Long = 0L

  def this() = this("", "", "")

  override def toString() = {
    "User:{id:%s,name:%s,email:%s,password:%s}"
      .format(id, name, email, password)
  }
}