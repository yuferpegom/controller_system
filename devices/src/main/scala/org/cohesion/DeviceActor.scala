package org.cohesion

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.Behaviors
import org.cohesion.model.Device
import java.util.UUID
import java.time.LocalDateTime
import org.cohesion.service.DeviceService
import org.apache.kafka.clients.producer.ProducerRecord
import akka.stream.scaladsl.Source
import org.cohesion.model.Thermostat
import org.cohesion.infrastructure.kafka.Publisher
import akka.stream.Materializer

object DeviceActor {

  trait Command
  final case class GenerateReading() extends Command

  def apply(
    service: DeviceService,
    producer: Publisher[String, String]
  )(
    implicit mat: Materializer
  ): Behavior[Command] = Behaviors.receiveMessage { case GenerateReading() =>
    val record = new ProducerRecord[String, String]("test", "value")
    val device = Thermostat()
    Source
      .single(service.generateReading(device))
      .map(_ => record)
      .runWith(producer.publish())
    println("I'm doing my job")
    Behaviors.same
  }

}
