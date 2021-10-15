package org.cohesion

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.Behaviors
import akka.stream.Materializer
import org.cohesion.infrastructure.kafka.Publisher
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
    service
      .generateReadingSource()
      .runWith(producer.publish())
    println("I'm doing my job")
    Behaviors.same
  }

}
