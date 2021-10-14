package org.cohesion.service

import org.cohesion.model.Device
import org.cohesion.model.DeviceReading
import java.time.LocalDateTime
import org.cohesion.model.Thermostat
import org.cohesion.model.AirMonitoring

trait DeviceService {
  def generateReading(device: Device): DeviceReading
}

final class DeviceServiceImpl extends DeviceService {

  private final val randomGenerator = scala.util.Random

  def generateReading(device: Device): DeviceReading = DeviceReading(
    deviceId = device.deviceId,
    curretnValue = randomGenerator.nextFloat(),
    unit = unit(device),
    timestamp = LocalDateTime.now(),
    version = 1,
  )

  private[service] def unit(device: Device): String = {
      device match {
          case _: Thermostat => "Farenheith"
          case _: AirMonitoring => "AQI"
          case _ => "Other"
      }
  }

}
