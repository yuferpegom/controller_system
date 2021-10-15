package org.cohesion.boot

import akka.kafka.ConsumerSettings
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

object Boot extends App {
  implicit val system = ActorSystem[Void](Behaviors.empty, "devices")
  val config = system.settings.config.getConfig("akka.kafka.producer")

  val consumerSettings = ConsumerSettings(config, new StringDeserializer, new ByteArrayDeserializer)
    .withBootstrapServers("http://localhost:9092")
    .withGroupId("group1")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

}
