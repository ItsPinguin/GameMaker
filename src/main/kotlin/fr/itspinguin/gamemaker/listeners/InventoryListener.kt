package fr.itspinguin.gamemaker.listeners

import fr.itspinguin.gamemaker.menus.MenuManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

object InventoryListener : Listener {
  @EventHandler
  fun click(e: InventoryClickEvent) {
    if (e.clickedInventory != e.view.topInventory) return
    val menuInstance = MenuManager.findMenuInstance(e.inventory, e.whoClicked as Player) ?: return
    MenuManager.click(e, menuInstance)
  }
}