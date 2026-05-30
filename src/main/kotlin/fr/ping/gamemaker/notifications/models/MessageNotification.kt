package fr.ping.gamemaker.notifications.models

import fr.ping.gamemaker.i18n.I18nManager
import org.bukkit.entity.Player

data class MessageNotification(
  var message: String,
) : Notification() {
  override fun notify(player: Player) {
    player.sendMessage(if (message.startsWith("$")) I18nManager[message.substring(1)] else message)
  }
}