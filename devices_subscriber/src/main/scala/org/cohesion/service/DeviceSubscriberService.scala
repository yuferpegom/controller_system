package org.cohesion.service

import org.cohesion.domain.DeviceReading
import org.cohesion.infrastructure.kafka.Consumer
import org.cohesion.infrastructure.model.devices.DeviceReadingMessage

trait DeviceSubscriberService {
  def processReading()

}

final class DeviceSubscriberServiceImpl(consumer: Consumer) extends DeviceSubscriberService {

  def consume() = consumer
    .consume()
    .map(record => DeviceReadingMessage.parseFrom(record.value()))
    .map(DeviceReading(_))

  override def processReading(): Unit = ???
}
