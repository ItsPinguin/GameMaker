package fr.ping.gamemaker.menus

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.actions.ActionContext
import fr.ping.gamemaker.actions.ActionManager
import fr.ping.gamemaker.i18n.I18nManager
import fr.ping.gamemaker.items.ItemBuilderContext
import fr.ping.gamemaker.items.ItemManager
import fr.ping.gamemaker.menus.models.MenuButton
import fr.ping.gamemaker.menus.models.MenuInstance
import fr.ping.gamemaker.menus.models.PageState
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
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
      val triggers : List<ClickType> = action.inventoryTriggers ?: listOf(e.click)
      if (!triggers.contains(e.click)) return@forEach
      ActionManager.executeAction(action, ActionContext.MenuClickActionContext(
        e.whoClicked as Player,
        menuInstance,
        e
      ))
    }
    if (slot.list != null && slot.pageOffset == null) {
      val builder = GameMakerPlugin.itemListBuilderRegistry.getResource(slot.list) ?: return
      val pageState = menuInstance.pageStates.getOrPut(slot.list!!) { PageState(0, 0) }
      val slots = slot.getFilledSlots()
      val index = slots.indexOf(e.slot)
      builder.onClick(
        index + pageState.getOffset(),
        e, menuInstance
      )
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
    val title = I18nManager.getComponentIfIndicator(
      I18nManager.playerLanguages[player.uniqueId] ?: I18nManager.config.defaultLanguage,
      template.title, System.currentTimeMillis())
    val inventory =
      if (template.inventoryType == InventoryType.CHEST)
      Bukkit.createInventory(null, template.rows * 9, title)
    else
      Bukkit.createInventory(null, template.inventoryType, title)

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
    template.contents.forEach { slot ->
      renderSlot(slot, player, menuInstance)
      slot.updateLater.forEach { time ->
        Bukkit.getScheduler().runTaskLater(GameMakerPlugin.getInstance(), Runnable {
          renderSlot(slot, player, menuInstance)
        }, time)
      }
    }
  }

  fun renderSlot(button: MenuButton, player: Player, menuInstance : MenuInstance) {
    val inventory = menuInstance.inventory ?: return
    val filledSlots = button.getFilledSlots()
    val listProvider = GameMakerPlugin.itemListBuilderRegistry.getResource(button.list)
    if (button.list != null && button.pageOffset == null) {
      menuInstance.pageStates.getOrPut(button.list!!) { PageState(0, 0) }.apply {
        pageSize = filledSlots.size
        println("Page size: $pageSize")
        total = listProvider!!.getListSize(ItemBuilderContext.MenuSlotItemBuilderContext(
          actualSlot = 0,
          index = 0,
          menuInstance = menuInstance,
          menuButton = button,
          player = player,
        ))
      }
    }
    filledSlots.forEachIndexed { index, slotIndex ->
      if (slotIndex !in 0 until inventory.size) return@forEachIndexed
      val context = ItemBuilderContext.MenuSlotItemBuilderContext(
        actualSlot = slotIndex,
        index = index,
        menuInstance = menuInstance,
        menuButton = button,
        player = player,
      )
      if (button.list == null) {
        inventory.setItem(
          slotIndex,
          ItemManager.buildItem(button.item?.get(),
            context)
        )
      } else {
        if (button.pageOffset != null) {
          inventory.setItem(slotIndex, listProvider
            ?.getPageTurnItem(button.pageOffset!!, menuInstance.pageStates[button.list!!]!!)
            ?: ItemManager.buildItem(button.item?.get(), context))
        } else {
          val listIndex = index + menuInstance.pageStates[button.list!!]!!.getOffset()
          inventory.setItem(slotIndex,
            listProvider?.getItem(listIndex, context) ?: ItemManager.buildItem(button.item?.get(), context))
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