package fr.ping.gamemaker.listeners

import fr.ping.gamemaker.menus.MenuManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

object InventoryListener : Listener {
  @EventHandler
  fun click(e: InventoryClickEvent) {
    val templateId = MenuManager.findTemplateId(e.inventory, e.whoClicked as Player) ?: return
    MenuManager.click(e, e.inventory, templateId)
  }
}