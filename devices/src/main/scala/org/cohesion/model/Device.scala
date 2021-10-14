package org.cohesion.model

import java.time.LocalDateTime

final case class Device(deviceId: String, name: String, createdAt: LocalDateTime)
