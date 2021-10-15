package org.cohesion.service

import akka.Done
import akka.kafka.scaladsl
import akka.stream.Materializer
import akka.stream.scaladsl.Flow
import akka.stream.scaladsl.Keep
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.cohesion.domain.DeviceReading
import org.cohesion.infrastructure.kafka.Consumer
import org.cohesion.infrastructure.model.devices.DeviceReadingMessage
import org.cohesion.repository.DeviceSubscriberRepository

import scala.concurrent.Future

trait DeviceSubscriberService {
  def consumeAndSave()(implicit mat: Materializer): Future[Done]

}

final class DeviceSubscriberServiceImpl(consumer: Consumer, repository: DeviceSubscriberRepository)
  extends DeviceSubscriberService {

  def consumeAndSave()(implicit mat: Materializer) = consumer
    .consume()
    .map(record => DeviceReadingMessage.parseFrom(record.value()))
    .map(DeviceReading(_))
    .runWith(processReading)

  private[service] def processReading =
    Flow[DeviceReading].mapAsync(1)(repository.save).toMat(Sink.ignore)(Keep.right)
}
