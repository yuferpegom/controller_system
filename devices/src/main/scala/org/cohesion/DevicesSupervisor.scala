package org.cohesion

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.Routers
import akka.actor.typed.SupervisorStrategy
import akka.actor.typed.Behavior
import akka.actor.typed.ActorRef
import scala.concurrent.duration.FiniteDuration
import akka.actor.Actor
import concurrent.duration._

object DevicesSupervisor {

  case class Start() extends DeviceActor.Command

  def active(routerWithBroadcast: ActorRef[DeviceActor.Command]): Behavior[DeviceActor.Command] =
    Behaviors.withTimers[DeviceActor.Command] { timers =>
      timers.startTimerAtFixedRate(Start(), 5.second)
      Behaviors.receiveMessage {
        case Start() =>
          routerWithBroadcast ! DeviceActor.GenerateReading()
          Behaviors.same
        case _ =>
          Behaviors.unhandled
      }
    }

  def apply(): Behavior[DeviceActor.Command] = Behaviors.setup[DeviceActor.Command] { ctx =>
    val pool =
      Routers.pool(poolSize = 5) {
        Behaviors.supervise(DeviceActor()).onFailure[Exception](SupervisorStrategy.restart)
      }

    val poolWithBroadcast = pool.withBroadcastPredicate(
      _.isInstanceOf[DeviceActor.GenerateReading]
    )
    val routerWithBroadcast = ctx.spawn(poolWithBroadcast, "pool-with-broadcast")
    active(routerWithBroadcast)
  }

}
