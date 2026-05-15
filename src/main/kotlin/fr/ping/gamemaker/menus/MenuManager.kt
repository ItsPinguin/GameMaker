package fr.ping.gamemaker.menus

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.actions.ActionContext
import fr.ping.gamemaker.actions.ActionManager
import fr.ping.gamemaker.items.ItemManager
import fr.ping.gamemaker.menus.models.MenuInstance
import fr.ping.gamemaker.menus.models.PageState
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import java.util.*

object MenuManager {
  var menus : MutableMap<UUID, MutableMap<String, MenuInstance>> = mutableMapOf()
  var lastOpened : MutableMap<UUID, String> = mutableMapOf()

  fun findTemplateId(inventory: Inventory, player: Player) : String? {
    val lastOpened = lastOpened[player.uniqueId] ?: return null
    if (inventory == menus[player.uniqueId]?.get(lastOpened))
      return lastOpened
    return null
  }

  fun findMenuInstance(inventory: Inventory, player: Player) : MenuInstance? {
    val lastOpened = lastOpened[player.uniqueId] ?: return null
    if (inventory == menus[player.uniqueId]?.get(lastOpened)?.inventory)
      return menus[player.uniqueId]?.get(lastOpened)
    return null
  }

  fun click(e: InventoryClickEvent, menuInstance: MenuInstance) {
    val template = menuInstance.template.resource ?: return
    val slot = template.getButton(e.slot)
    e.isCancelled = slot.cancel ?: template.cancelByDefault
    slot.actions.forEach { action ->
      ActionManager.executeAction(action, ActionContext.MenuClickActionContext(
        e.whoClicked as Player,
        menuInstance,
        e
      ))
    }
  }

  fun open(player: Player, templateId: String, data: Map<String, Any?> = mapOf()) {
    val templateHandle = GameMakerPlugin.menuTemplateRegistry.getResourceHandle(templateId) ?: return
    val template = templateHandle.resource ?: return
    if (template.isStatic && menus.getOrPut(player.uniqueId) { mutableMapOf() }[templateId] != null) {
      val menuInstance = menus[player.uniqueId]?.get(templateId)!!
      player.openInventory(menuInstance.inventory!!)
      lastOpened[player.uniqueId] = templateId
    }
    val inventory =
      if (template.inventoryType == InventoryType.CHEST)
      Bukkit.createInventory(null, template.rows * 9, Component.text(template.title))
    else
      Bukkit.createInventory(null, template.inventoryType, Component.text(template.title))

    val menuInstance = menus.getOrPut(player.uniqueId) { mutableMapOf() }.getOrPut(templateId) { MenuInstance(templateHandle, inventory) }
    menuInstance.inventory = inventory
    menuInstance.template = templateHandle
    menuInstance.data = data
    player.openInventory(inventory)
    lastOpened[player.uniqueId] = templateId

    renderMenu(player, menuInstance, data)
  }

  fun renderMenu(player: Player, menuInstance: MenuInstance, context: Map<String, Any?> = mapOf()) {
    val template = menuInstance.template.resource ?: return
    val inventory = menuInstance.inventory ?: return
    template.contents.forEach { slot ->
      val filledSlots = slot.getFilledSlots()
      val listProvider = GameMakerPlugin.itemListProviderRegistry.getResource(slot.list)
      if (slot.list != null && slot.pageOffset == null) {
        menuInstance.pageStates.getOrPut(slot.list!!) { PageState(0, 0) }.apply {
          pageSize = filledSlots.size
          println("Page size: $pageSize")
          total = listProvider!!.getListSize()
        }
      }
      filledSlots.forEachIndexed { index, slotIndex ->
        if (slotIndex >= 0 && slotIndex < inventory.size) {
          if (slot.list == null) {
            inventory.setItem(
              slotIndex,
              ItemManager.buildItem(slot.item?.get(), slot.context.apply {
                put("player", player)
                put("menu_instance", menuInstance)
                put("slots", slot)
                put("slot", slotIndex)
              })
            )
          } else {
            if (slot.pageOffset != null) {
              inventory.setItem(slotIndex, listProvider
                ?.getPageTurnItem(slot.pageOffset!!, menuInstance.pageStates[slot.list!!]!!)
                ?: ItemManager.buildItem(slot.item?.get(), slot.context.apply {
                  put("player", player)
                  put("menu_instance", menuInstance)
                  put("slots", slot)
                  put("slot", slotIndex)
                }))
            } else {
              val listIndex = index + menuInstance.pageStates[slot.list!!]!!.getOffset()
              inventory.setItem(slotIndex,
                listProvider?.getItem(listIndex, slot.context.apply {
                  put("player", player)
                  put("menu_instance", menuInstance)
                  put("slots", slot)
                  put("index", index)
                }) ?: ItemManager.buildItem(slot.item?.get(), slot.context.apply {
                  put("player", player)
                  put("menu_instance", menuInstance)
                  put("slots", slot)
                  put("index", index)
                }))
            }
          }
        }
      }
    }
  }

  fun changePage(player: Player, menuInstance : MenuInstance, list : String, offset : Int) {
    println("Changing page")
    menuInstance.pageStates.getOrPut(list) { PageState(0, 0) }.apply {
      if (getOffset() + offset*pageSize !in 0..total) let {
        println("Out of bounds")
        return
      }
      println("Changing page to ${page + offset}")
      page += offset
    }
    renderMenu(player, menuInstance)
    player.updateInventory()
    println("DONE")
  }
}