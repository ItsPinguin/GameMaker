package fr.ping.gamemaker.listeners

import fr.ping.gamemaker.menus.MenuManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

object InventoryListener : Listener {
  @EventHandler
  fun click(e: InventoryClickEvent) {
    val menuInstance = MenuManager.findMenuInstance(e.inventory, e.whoClicked as Player) ?: let {
      println("No menu instance found for inventory ${e.inventory} and player ${e.whoClicked.name}")
      return
    }
    MenuManager.click(e, menuInstance)
  }
}