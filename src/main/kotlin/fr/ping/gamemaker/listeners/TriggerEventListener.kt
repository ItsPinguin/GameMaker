package fr.ping.gamemaker.listeners

import fr.ping.gamemaker.triggers.TriggerManager
import org.bukkit.entity.Interaction
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

object TriggerEventListener : Listener {
  @EventHandler
  fun clickEvent(e: PlayerInteractEvent) {
    TriggerManager.trigger("player_click", mapOf("player" to e.player, "event" to e))
  }

  @EventHandler
  fun interactEntity(e: PlayerInteractEntityEvent) {
    //GameMakerPlugin.getInstance().logger.info("[Trigger] Entity clicked. Was interaction: ${e.rightClicked is Interaction}, was of type ${e.rightClicked.javaClass.simpleName}")
    if (e.rightClicked is Interaction)
      TriggerManager.trigger("interaction", mapOf(
        "player" to e.player,
        "event" to e,
        "entity" to e.rightClicked))
  }
}