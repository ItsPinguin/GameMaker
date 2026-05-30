package fr.ping.gamemaker.notifications.models

import fr.ping.gamemaker.GameMakerPlugin
import org.bukkit.Sound
import org.bukkit.entity.Player

data class SoundNotification(
  var sound: Sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
  var volume: Float = 1f,
  var pitch: Float = 1f,
) : Notification() {
  override fun notify(player: Player) {
    player.playSound(player.location, sound, volume, pitch)

  }

  fun notifyNow(player: Player) {
    if (delay > 0) player.server.scheduler.runTaskLater(
      GameMakerPlugin.getInstance(),
      Runnable {
        notifyNow(player)
      },
      delay
    ) else notifyNow(player)
  }
}
