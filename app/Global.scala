import org.squeryl.Session
import org.squeryl.SessionFactory
import org.squeryl.adapters.H2Adapter
import org.squeryl.adapters.MySQLAdapter
import org.squeryl.adapters.PostgreSqlAdapter
import org.squeryl.internals.DatabaseAdapter
import play.api.Application
import play.api.GlobalSettings
import play.api.Logger
import play.api.db.DB
import play.api.mvc.WithFilters
import play.filters.csrf.CSRFFilter
import play.api.Play

object Global extends WithFilters(CSRFFilter()) with GlobalSettings {
  private val LOG = Logger(getClass)

  override def onStart(app: Application) {
    
    LOG.info("DB session init..")
    var driver = app.configuration.getString("db.default.driver")
    LOG.debug("Mysql driver: " + driver)
    SessionFactory.concreteFactory = driver match {
      case Some("com.mysql.jdbc.Driver") => Some(() => getSession(new MySQLAdapter, app))
      case Some("org.h2.Driver") => Some(() => getSession(new H2Adapter, app))
      case Some("org.postgresql.Driver") => Some(() => getSession(new PostgreSqlAdapter, app))
      case _ => sys.error("Database driver must be either (org.h2.Driver, org.postgresql.Driver, com.mysql.jdbc.Driver)")
    }

    // Generate DB tables if they are not exists 
    //    transaction {
    //      Schema.create
    //    }

    //    LOG.info("Akka init.. ")
    //    Akka.system.scheduler.schedule(5 second, 2 second) {
    //      LOG.info("Akka scheduler")
    //    }
  }

  def getSession(adapter: DatabaseAdapter, app: Application) = Session.create(DB.getConnection()(app), adapter)

}