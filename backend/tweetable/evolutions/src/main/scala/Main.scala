package evolutions

import play.api.db.Database
import play.api.db.Databases
import play.api.db.evolutions.Evolutions
import com.typesafe.config.ConfigFactory

object Main extends App:

  val conf = ConfigFactory.load().getConfig("db.default")
  // println(conf)

  val driver = conf.getString("driver")
  val url = conf.getString("url")
  val username = conf.getString("username")
  val password = conf.getString("password")

  Class.forName(driver)
  val c = java.sql.DriverManager.getConnection(url, username, password)
  c.close()

  val map = List("driver", "url", "username", "password").map(key => key -> conf.getString(key)).toMap
  val database: Database = Databases(driver, url, name = "default", config = map)
  Evolutions.applyEvolutions(database)

  println("🆗")
