package org.cohesion

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.Behaviors
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import cats.syntax.option._
import com.google.protobuf.timestamp.Timestamp
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.cohesion.infrastructure.kafka.Publisher
import org.cohesion.infrastructure.model.devices.DeviceReadingMessage
import org.cohesion.model.Device
import org.cohesion.model.Thermostat
import org.cohesion.service.DeviceService

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

object DeviceActor {

  trait Command
  final case class GenerateReading() extends Command

  def apply(
    service: DeviceService,
    producer: Publisher[String, Array[Byte]],
  )(
    implicit mat: Materializer
  ): Behavior[Command] = Behaviors.receiveMessage { case GenerateReading() =>
    val device = Thermostat()
    Source
      .single(service.generateReading(device))
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
      .runWith(producer.publish())
    println("I'm doing my job")
    Behaviors.same
  }

}
