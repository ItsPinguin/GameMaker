package fr.ping.gamemaker.notifications.models

import com.google.gson.annotations.SerializedName
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

data class TitleNotification(
  var title: String? = null,
  var subtitle: String? = null,
  @SerializedName("action_bar", alternate = ["actionbar", "actionBar"])
  var actionBar: String? = null,
  var fadeIn: Int = 0,
  var stay: Int = 0,
  var fadeOut: Int = 0,
) : Notification() {
  override fun notify(player: Player) {
    if (!title.isNullOrEmpty() || !subtitle.isNullOrEmpty()) {
      player.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
    }
    if (!actionBar.isNullOrEmpty())
      player.sendActionBar(Component.text(actionBar!!))
  }
}
