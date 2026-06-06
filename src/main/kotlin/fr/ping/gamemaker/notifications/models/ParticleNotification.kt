package fr.ping.gamemaker.notifications.models

import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.util.Vector

data class ParticleNotification(
  var particle: Particle = Particle.HAPPY_VILLAGER,
  var count: Int = 1,
  var delta: Vector = Vector(1, 1, 1),
  var offset: Vector = Vector(0, 0, 0),
  var speed: Double = 1.0,
) : Notification() {
  override fun notify(player: Player) {
    player.world.spawnParticle(particle, player.location.clone().add(offset), count, delta.x, delta.y, delta.z, speed)
  }
}