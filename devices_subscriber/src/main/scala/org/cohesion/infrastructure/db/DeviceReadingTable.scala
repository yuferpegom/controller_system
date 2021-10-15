package org.cohesion.infrastructure.db

import org.cohesion.infrastructure.db.model.DeviceReadingDTO
import java.time.LocalDateTime
import slick.jdbc.H2Profile.api._

class DeviceReadingTable(tag: Tag) extends Table[DeviceReadingDTO](tag, "device_readings") {

  def id = column[String]("id", O.PrimaryKey)
  def currentValue = column[Float]("currentValue")
  def unit = column[String]("unit")
  def timestamp = column[LocalDateTime]("timestamp")
  def version = column[Float]("version")

  def * =
    (
      id,
      currentValue,
      unit,
      timestamp.?,
      version,
    ) <> (DeviceReadingDTO.tupled, DeviceReadingDTO.unapply)

}
