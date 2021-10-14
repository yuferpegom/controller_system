package org.cohesion.boot

import akka.actor.typed.ActorSystem
import org.cohesion.DeviceActor
import org.cohesion.DevicesSupervisor
import concurrent.duration._
import akka.kafka.ProducerSettings
import org.apache.kafka.common.serialization.StringSerializer
import akka.actor.typed.javadsl.Behaviors
import org.cohesion.service.DeviceServiceImpl
import akka.stream.ActorMaterializer
import org.cohesion.infrastructure.kafka.PublisherImpl

object Boot extends App {
  implicit val system = ActorSystem[Void](Behaviors.empty, "")
  val config = system.settings.config.getConfig("akka.kafka.producer")
  val producerSettings = ProducerSettings(config, new StringSerializer, new StringSerializer)
    .withBootstrapServers("http://localhost:9092")

  val service = new DeviceServiceImpl
  val publisher = new PublisherImpl[String, String](producerSettings)
  val requester: ActorSystem[DeviceActor.Command] = ActorSystem(DevicesSupervisor(service, publisher), "supervisor")
}
