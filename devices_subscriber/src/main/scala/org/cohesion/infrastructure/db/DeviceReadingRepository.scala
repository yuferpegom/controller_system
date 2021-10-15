package org.cohesion.infrastructure.db

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import org.cohesion.infrastructure.db.Database
import org.cohesion.infrastructure.db.model.DeviceReadingDTO

class DeviceReadingRepository(val config: DatabaseConfig[JdbcProfile]) extends Database {
  import config.driver.api._
  import scala.concurrent.ExecutionContext.Implicits.global

  private final val deviceReadingTable = TableQuery[DeviceReadingTable]
  def insert(deviceReading: DeviceReadingDTO) = db.run(deviceReadingTable += deviceReading)
}
