package fr.itspinguin.gamemaker.notifications.models

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
}
