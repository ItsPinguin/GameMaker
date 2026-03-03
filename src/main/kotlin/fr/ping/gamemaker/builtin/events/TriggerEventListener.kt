package fr.ping.gamemaker.builtin.events

import fr.ping.gamemaker.addon.AddonManager
import org.bukkit.entity.Interaction
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

object TriggerEventListener : Listener {
  @EventHandler
  fun clickEvent(e: PlayerInteractEvent) {
    AddonManager.TriggerManager.trigger("player_click", mapOf("player" to e.player, "event" to e))
  }

  @EventHandler
  fun interactEntity(e: PlayerInteractEntityEvent) {
    //GameMakerPlugin.getInstance().logger.info("[Trigger] Entity clicked. Was interaction: ${e.rightClicked is Interaction}, was of type ${e.rightClicked.javaClass.simpleName}")
    if (e.rightClicked is Interaction)
      AddonManager.TriggerManager.trigger("interaction", mapOf(
        "player" to e.player,
        "event" to e,
        "entity" to e.rightClicked))
  }
}