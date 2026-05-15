package fr.ping.gamemaker.menus

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class TestListProvider : ItemListProvider() {
  override fun getListSize(): Int = 100
  override fun getItem(index: Int, context: Map<String, Any?>): ItemStack? {
    if (index >= 100) return null
    val itemStack = ItemStack(Material.STONE)
    val itemMeta = itemStack.itemMeta ?: return itemStack
    itemMeta.setItemName("§aINDEX: §e$index")
    itemStack.itemMeta = itemMeta
    return itemStack
  }
}