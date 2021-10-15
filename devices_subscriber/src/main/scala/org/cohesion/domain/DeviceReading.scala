package org.cohesion.domain

import org.cohesion.infrastructure.model.devices.DeviceReadingMessage

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

sealed abstract case class DeviceReading private (
  deviceId: String,
  currentValue: Float,
  unit: String,
  timestamp: Option[LocalDateTime],
  version: Float,
)

object DeviceReading {

  def apply(message: DeviceReadingMessage) = {
    val date = message
      .timestamp
      .map(ts =>
        Instant
          .ofEpochSecond(ts.seconds, ts.nanos)
          .atZone(ZoneOffset.UTC)
          .toLocalDateTime()
      )
    new DeviceReading(
      deviceId = message.deviceId,
      currentValue = message.curentValue,
      unit = message.unit,
      timestamp = date,
      version = message.version,
    ) {}
  }

}
