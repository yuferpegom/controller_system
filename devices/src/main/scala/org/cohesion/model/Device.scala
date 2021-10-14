package org.cohesion.model

import java.time.LocalDateTime
import java.util.UUID

sealed trait Device {
  val deviceId: String
  val name: String
  val createdAt: LocalDateTime
}

final case class Thermostat(
  override val deviceId: String = UUID.randomUUID.toString,
  override val name: String = "termosthat",
  override val createdAt: LocalDateTime = LocalDateTime.now(),
) extends Device

final case class AirMonitoring(
  override val deviceId: String = UUID.randomUUID.toString,
  override val name: String = "air-monitor",
  override val createdAt: LocalDateTime = LocalDateTime.now(),
) extends Device

final case class OtherDevice(
  override val deviceId: String = UUID.randomUUID.toString,
  override val name: String = "other-device",
  override val createdAt: LocalDateTime = LocalDateTime.now(),
) extends Device
