package org.cohesion.infrastructure.kafka.model

import java.time.LocalDateTime

final case class DeviceReading(
  deviceId: String,
  curretnValue: Float,
  unit: String,
  timestamp: LocalDateTime,
  version: Float,
)
