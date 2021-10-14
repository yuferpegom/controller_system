package org.cohesion.infrastructure.kafka

import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Sink
import org.apache.kafka.clients.producer.ProducerRecord
import scala.concurrent.Future
import akka.Done

trait Publisher[K, V] {
    def publish(): Sink[ProducerRecord[K, V], Future[Done]]
}

class PublisherImpl[K, V](producerSettings: ProducerSettings[K, V]) extends Publisher[K, V] {

    override def publish(): Sink[ProducerRecord[K, V], Future[Done]] = 
        Producer.plainSink(producerSettings)
  
}
