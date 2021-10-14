package org.cohesion.boot

import akka.actor.typed.ActorSystem
import org.cohesion.DeviceActor
import org.cohesion.DevicesSupervisor
import concurrent.duration._

object Boot extends App {
  val requester: ActorSystem[DeviceActor.Command] = ActorSystem(DevicesSupervisor(), "supervisor")
}
