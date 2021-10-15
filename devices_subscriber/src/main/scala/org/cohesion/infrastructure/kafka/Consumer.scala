package org.cohesion.infrastructure.kafka

import akka.kafka.ConsumerSettings
import akka.kafka.Subscriptions
import akka.kafka.scaladsl.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import akka.stream.scaladsl.Source
import scala.concurrent.Future
import akka.Done

trait Consumer {

  def consume(): Source[ConsumerRecord[String, Array[Byte]], Consumer.Control]

}

final class ConsumerImpl(consumerSettings: ConsumerSettings[String, Array[Byte]]) extends Consumer {

  override def consume(): Source[ConsumerRecord[String, Array[Byte]], Consumer.Control] =
    Consumer.plainSource(settings = consumerSettings, Subscriptions.topics(s"device-reading"))
}
