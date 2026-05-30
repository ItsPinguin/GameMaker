package fr.ping.gamemaker.notifications.models

import fr.ping.gamemaker.GameMakerPlugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player

data class ComposedNotification (
  var sounds : List<SoundNotification> = listOf(),
  var messages : List<MessageNotification> = listOf(),
  var titles : List<TitleNotification> = listOf(),
) : Notification() {
  override fun notify(player: Player) {
    val notifications = listOf(sounds, messages, titles).flatten()
    notifications.forEach { notifyWithDelay(player, it) }
  }

  fun notifyWithDelay(player: Player, notification: Notification) {
    if (notification.delay > 0) Bukkit.getScheduler().runTaskLater(
      GameMakerPlugin.getInstance(),
      Runnable {
        notification.notify(player)
      },
      notification.delay
    )
    else notification.notify(player)
  }
}