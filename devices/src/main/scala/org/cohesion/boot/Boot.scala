package org.cohesion.boot

import akka.actor.typed.ActorSystem
import akka.actor.typed.javadsl.Behaviors
import akka.kafka.ProducerSettings
import akka.stream.ActorMaterializer
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.common.serialization.StringSerializer
import org.cohesion.DeviceActor
import org.cohesion.DevicesSupervisor
import org.cohesion.infrastructure.kafka.PublisherImpl
import org.cohesion.service.DeviceServiceImpl

import concurrent.duration._

object Boot extends App {
  implicit val system = ActorSystem[Void](Behaviors.empty, "devices")
  val config = system.settings.config.getConfig("akka.kafka.producer")
  val producerSettings = ProducerSettings(config, new StringSerializer, new ByteArraySerializer())
    .withBootstrapServers("http://localhost:9092")

  val service = new DeviceServiceImpl
  val publisher = new PublisherImpl(producerSettings)
  val requester: ActorSystem[DeviceActor.Command] = ActorSystem(DevicesSupervisor(service, publisher), "supervisor")
}
