package org.cohesion.service

import org.cohesion.domain.DeviceReading
import org.cohesion.infrastructure.kafka.Consumer
import org.cohesion.infrastructure.model.devices.DeviceReadingMessage
import org.cohesion.repository.DeviceSubscriberRepository
import scala.concurrent.Future
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.consumer.ConsumerRecord
import akka.kafka.scaladsl

trait DeviceSubscriberService {
  def processReading(deviceReading: DeviceReading): Future[Int]
  def consume(): Source[DeviceReading, scaladsl.Consumer.Control]

}

final class DeviceSubscriberServiceImpl(consumer: Consumer, repository: DeviceSubscriberRepository) extends DeviceSubscriberService {

  override def consume(): Source[DeviceReading, scaladsl.Consumer.Control] = consumer
    .consume()
    .map(record => DeviceReadingMessage.parseFrom(record.value()))
    .map(DeviceReading(_))

  override def processReading(deviceReading: DeviceReading): Future[Int] = repository.save(deviceReading)
}
