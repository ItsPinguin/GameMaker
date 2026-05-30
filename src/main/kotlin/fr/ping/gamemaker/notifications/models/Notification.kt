package fr.ping.gamemaker.notifications.models

import fr.ping.utils.resources.Resource
import org.bukkit.entity.Player

abstract class Notification : Resource() {
  abstract fun notify(player : Player)

  var delay : Long = 0
}