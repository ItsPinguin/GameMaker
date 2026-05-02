package fr.ping.gamemaker.menus

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.actions.ActionManager
import fr.ping.gamemaker.items.ItemManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import java.util.UUID

object MenuManager {
  var menus : MutableMap<UUID, MutableMap<String, Inventory>> = mutableMapOf()
  var lastOpened : MutableMap<UUID, String> = mutableMapOf()

  fun findTemplateId(inventory: Inventory, player: Player) : String? {
    val lastOpened = lastOpened[player.uniqueId] ?: return null
    if (inventory == menus[player.uniqueId]?.get(lastOpened))
      return lastOpened
    return null
  }

  fun click(e: InventoryClickEvent, inventory: Inventory, templateId: String) {
    val template = GameMakerPlugin.menuTemplateRegistry.getResource(templateId) ?: return
    val slot = template.getButton(e.slot)
    e.isCancelled = slot?.cancel ?: template.cancelByDefault
    if (slot == null) return
    slot.actions.forEach { action ->
      println("Executing action: ${action.action} for slot ${e.slot}")
      ActionManager.executeAction(action, mapOf(
        "player" to e.whoClicked as Player,
        "inventory" to inventory,
        "slot" to e.slot,
        "action" to action
      ))
    }
  }

  fun open(player: Player, templateId: String) {
    val template = GameMakerPlugin.menuTemplateRegistry.getResource(templateId) ?: return
    if (template.isStatic && menus.getOrPut(player.uniqueId) { mutableMapOf() }[templateId] != null) {
      val inventory = menus[player.uniqueId]?.get(templateId)!!
      player.openInventory(inventory)
      lastOpened[player.uniqueId] = templateId
    }
    val inventory =
      if (template.inventoryType == InventoryType.CHEST)
      Bukkit.createInventory(null, template.rows * 9, Component.text(template.title))
    else
      Bukkit.createInventory(null, template.inventoryType, Component.text(template.title))

    menus.getOrPut(player.uniqueId) { mutableMapOf() }[templateId] = inventory
    player.openInventory(inventory)
    lastOpened[player.uniqueId] = templateId

    template.contents.forEach { slot ->
      val item = ItemManager.buildItem(slot.item?.get())
      slot.getFilledSlots().forEach { index ->
        if (index >= 0 && index < inventory.size) inventory.setItem(index, item)
      }
    }
  }
}