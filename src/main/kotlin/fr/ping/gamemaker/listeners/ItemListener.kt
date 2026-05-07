package fr.ping.gamemaker.listeners

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.actions.ActionManager
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.items.ItemManager
import fr.ping.utils.resources.ResourceManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object ItemListener : Listener {
  @EventHandler
  fun onInteract(e : PlayerInteractEvent) {
    val item = ItemManager.getItemId(e.item ?: return)
    val itemTemplate = GameMakerPlugin.itemTemplateRegistry.getResource(item) ?: return
    val actions : List<Action> = ResourceManager.parseAny<List<Action>>(itemTemplate.data["actions"] ?: return) ?: return
    actions.forEach {
      ActionManager.executeAction(it, mapOf("player" to e.player))
    }
  }
}