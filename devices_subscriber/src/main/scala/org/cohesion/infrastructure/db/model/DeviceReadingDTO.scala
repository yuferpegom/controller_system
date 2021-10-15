package org.cohesion.infrastructure.db.model

import java.time.LocalDateTime
import org.cohesion.domain.DeviceReading

case class DeviceReadingDTO(
  deviceId: String,
  currentValue: Float,
  unit: String,
  timestamp: Option[LocalDateTime],
  version: Float,
)

object DeviceReadingDTO {

  def fromDomain(deviceReading: DeviceReading): DeviceReadingDTO = DeviceReadingDTO(
    deviceId = deviceReading.deviceId,
    currentValue = deviceReading.currentValue,
    unit = deviceReading.unit,
    timestamp = deviceReading.timestamp,
    version = deviceReading.version,
  )

  def tupled = (DeviceReadingDTO.apply _).tupled
}
