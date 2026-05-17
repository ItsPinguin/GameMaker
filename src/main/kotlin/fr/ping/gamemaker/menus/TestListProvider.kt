package fr.ping.gamemaker.menus

import fr.ping.gamemaker.items.ItemBuilderContext
import fr.ping.gamemaker.items.builders.models.ItemListBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class TestListProvider : ItemListBuilder() {
  override fun getListSize(context: ItemBuilderContext): Int = 100
  override fun getItem(index: Int, context: ItemBuilderContext): ItemStack? {
    if (index >= 100) return null
    val itemStack = ItemStack(Material.STONE)
    val itemMeta = itemStack.itemMeta ?: return itemStack
    itemMeta.setItemName("§aINDEX: §e$index")
    itemStack.itemMeta = itemMeta
    return itemStack
  }
}