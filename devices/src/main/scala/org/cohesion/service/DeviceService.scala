package org.cohesion.service

import akka.NotUsed
import akka.stream.scaladsl.Source
import cats.syntax.option.catsSyntaxOptionId
import com.google.protobuf.timestamp.Timestamp
import org.apache.kafka.clients.producer.ProducerRecord
import org.cohesion.infrastructure.model.devices.DeviceReadingMessage
import org.cohesion.model.AirMonitoring
import org.cohesion.model.Device
import org.cohesion.model.DeviceReading
import org.cohesion.model.Thermostat

import java.time.LocalDateTime
import java.time.ZoneOffset

trait DeviceService {
  def generateReadingSource(): Source[ProducerRecord[String, Array[Byte]], NotUsed]
}

final class DeviceServiceImpl extends DeviceService {

  private final val randomGenerator = scala.util.Random

  private[service] def generateReading(device: Device): DeviceReading = DeviceReading(
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

  override def generateReadingSource(): Source[ProducerRecord[String, Array[Byte]], NotUsed] = {
    val device = Thermostat() // TODO: issue created to generate random device readings
    Source
      .single(generateReading(device))
      .map { deviceReading =>
        val instant = deviceReading.timestamp.toInstant(ZoneOffset.UTC);
        val timestamp = Timestamp.of(instant.getEpochSecond(), instant.getNano())

        DeviceReadingMessage(
          deviceReading.deviceId,
          deviceReading.curretnValue,
          deviceReading.unit,
          timestamp.some,
          deviceReading.version,
        )

      }
      .map(message =>
        new ProducerRecord[String, Array[Byte]]("device-reading", message.toByteArray)
      )
  }

}
