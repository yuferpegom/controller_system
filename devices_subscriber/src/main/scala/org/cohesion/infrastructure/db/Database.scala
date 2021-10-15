package org.cohesion.infrastructure.db

import org.cohesion.infrastructure.db.DbConfiguration
import slick.jdbc.JdbcProfile
import slick.basic.DatabaseConfig

trait Database {
  val config: DatabaseConfig[JdbcProfile]
  val db: JdbcProfile#Backend#Database = config.db
}
