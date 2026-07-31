package fr.itspinguin.gamemaker.notifications.models

import fr.itspinguin.resourcemanager.Resource
import org.bukkit.entity.Player

abstract class Notification : Resource() {
  abstract fun notify(player : Player)

  var delay : Long = 0
}