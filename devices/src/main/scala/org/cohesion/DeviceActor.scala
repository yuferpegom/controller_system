package org.cohesion

import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.Behaviors

object DeviceActor {

  trait Command
  final case class GenerateReading() extends Command

  def apply(): Behavior[Command] =
    Behaviors.receiveMessage { case GenerateReading() =>
      println("I'm doing my job")
      Behaviors.same
    }

}
