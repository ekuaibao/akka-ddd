package pl.newicom.dddd.cluster

import akka.cluster.sharding.ShardRegion._
import pl.newicom.dddd.cluster.ShardResolution._
import pl.newicom.dddd.aggregate.Command
import pl.newicom.dddd.messaging.AddressableMessage
import pl.newicom.dddd.messaging.command.CommandMessage
import pl.newicom.dddd.messaging.correlation.EntityIdResolution
import pl.newicom.dddd.messaging.correlation.EntityIdResolution.EntityIdResolver

object ShardResolution {
  type ShardResolutionStrategy = EntityIdResolver => ExtractShardId
}

trait ShardResolution[A] extends EntityIdResolution[A] {

  def shardResolutionStrategy: ShardResolutionStrategy

  def shardResolver: ExtractShardId = shardResolutionStrategy(entityIdResolver)

  val idExtractor: ExtractEntityId = {
    case msg: AddressableMessage => (entityIdResolver(msg), msg)
    case c: Command => (entityIdResolver(c), CommandMessage(c))
  }

}

