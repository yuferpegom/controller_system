package org.cohesion.service

import org.scalatest.flatspec.AnyFlatSpec
import org.cohesion.infrastructure.kafka.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.cohesion.infrastructure.model.devices.DeviceReadingMessage
import java.util.UUID
import java.time.LocalDate
import java.time.LocalDateTime
import com.google.protobuf.timestamp.Timestamp
import akka.stream.scaladsl.Source
import cats.syntax.option.catsSyntaxOptionId
import akka.kafka.scaladsl
import org.cohesion.repository.DeviceSubscriberRepositoryImpl
import org.cohesion.infrastructure.db.DeviceReadingRepository
import org.cohesion.repository.DeviceSubscriberRepository
import org.cohesion.domain.DeviceReading
import scala.concurrent.Future
import akka.actor.ActorSystem
import akka.stream.Materializer
import org.scalatest.concurrent.ScalaFutures
import akka.Done
import org.scalatest.matchers.should.Matchers

// Note I avoid using testing frameworks as mockito or. The reason is that those frameworks are harder to debug.
// By creating my own mocks I can understand better what's going on
// However, this also have its downsides as some scenarios are harder to simulate 
class DeviceSubscriberServiceSpec extends AnyFlatSpec with ScalaFutures with Matchers {
  "A service" should "consume and save a record" in {
    val date = LocalDateTime.now()
    val ts = Timestamp(date.getSecond(), date.getNano())
    val message =
      new DeviceReadingMessage(
        deviceId = UUID.randomUUID().toString(),
        curentValue = util.Random.nextFloat(),
        unit = "Farenheit",
        timestamp = ts.some,
        version = 0.01f,
      )
    val record = new ConsumerRecord("test", 0, 1L, "test", message.toByteArray)
    val consumer =
      new Consumer {
        def consume(): Source[ConsumerRecord[String, Array[Byte]], scaladsl.Consumer.Control] =
          Source.single(record)
      }

    val repository =
      new DeviceSubscriberRepository {
        override def save(deviceReading: DeviceReading) = Future.successful(1)
      }
    val service = new DeviceSubscriberServiceImpl(consumer, repository)

    whenReady(service.consumeAndSave()) { result => result shouldBe Done}
  }

}
