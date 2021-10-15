package org.cohesion.repository

import org.cohesion.domain.DeviceReading
import org.cohesion.infrastructure.db.DeviceReadingRepository
import org.cohesion.infrastructure.db.model.DeviceReadingDTO
import scala.concurrent.Future

trait DeviceSubscriberRepository {
  def save(deviceReading: DeviceReading): Future[Int]
}

class DeviceSubscriberRepositoryImpl(h2Repo: DeviceReadingRepository)
  extends DeviceSubscriberRepository {

  override def save(
    deviceReading: DeviceReading
  ): Future[Int] = h2Repo.insert(DeviceReadingDTO.fromDomain(deviceReading))

}
