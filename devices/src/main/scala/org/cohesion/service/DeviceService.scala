package org.cohesion.service

import org.cohesion.model.AirMonitoring
import org.cohesion.model.Device
import org.cohesion.model.DeviceReading
import org.cohesion.model.Thermostat

import java.time.LocalDateTime
import java.time.ZoneOffset

trait DeviceService {
  def generateReading(device: Device): DeviceReading
}

final class DeviceServiceImpl extends DeviceService {

  private final val randomGenerator = scala.util.Random

  def generateReading(device: Device): DeviceReading = DeviceReading(
    deviceId = device.deviceId,
    curretnValue = randomGenerator.nextFloat(),
    unit = unit(device),
    timestamp = LocalDateTime.now(ZoneOffset.UTC),
    version = 1,
  )

  private[service] def unit(device: Device): String =
    device match {
      case _: Thermostat =>
        "Farenheith"
      case _: AirMonitoring =>
        "AQI"
      case _ =>
        "Other"
    }

}
