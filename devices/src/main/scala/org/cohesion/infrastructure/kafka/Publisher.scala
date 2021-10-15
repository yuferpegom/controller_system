package org.cohesion.infrastructure.kafka

import akka.Done
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Sink
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.ByteArraySerializer

import scala.concurrent.Future

trait Publisher[K, V] {
  def publish(): Sink[ProducerRecord[K, V], Future[Done]]
}

class PublisherImpl(producerSettings: ProducerSettings[String, Array[Byte]])
  extends Publisher[String, Array[Byte]] {

  override def publish(
  ): Sink[ProducerRecord[String, Array[Byte]], Future[Done]] = Producer.plainSink(producerSettings)

}
