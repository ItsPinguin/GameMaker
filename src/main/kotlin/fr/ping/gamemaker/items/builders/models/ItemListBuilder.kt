package fr.ping.gamemaker.items.builders.models

import fr.ping.gamemaker.items.ItemBuilderContext
import fr.ping.gamemaker.menus.models.MenuInstance
import fr.ping.gamemaker.menus.models.PageState
import fr.ping.utils.resources.Resource
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

open class ItemListBuilder : Resource() {
  open fun getItem(index: Int, context : ItemBuilderContext) : ItemStack? = null

  open fun getListSize(context : ItemBuilderContext) : Int = 0

  open fun getPageTurnItem(pageOffset : Int, pageState: PageState) : ItemStack? {
    if (pageState.page * pageState.pageSize + pageOffset * pageState.pageSize !in 0..<pageState.total) return null
    val itemStack = ItemStack(Material.ARROW)
    val itemMeta = itemStack.itemMeta ?: return itemStack
    itemMeta.lore = listOf(
      "§e§lCLICK §7to go to page ${pageOffset + pageState.page + 1}/${(pageState.total/pageState.pageSize) + 1}"
    )
    itemMeta.setItemName("§aTurn Page")
    itemStack.itemMeta = itemMeta
    return itemStack
  }

  open fun onClick(index: Int, event: InventoryClickEvent, menuInstance: MenuInstance) {
    println("Clicked on $index, actual slot is ${event.slot}")
  }
}