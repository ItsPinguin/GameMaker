package fr.itspinguin.gamemaker.menus

import fr.itspinguin.gamemaker.items.ItemBuilderContext
import fr.itspinguin.gamemaker.items.builders.models.ItemListBuilder
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class TestListProvider : ItemListBuilder() {
  override fun getListSize(context: ItemBuilderContext): Int = 100
  override fun getItem(index: Int, context: ItemBuilderContext): ItemStack? {
    if (index >= 100) return null
    val itemStack = ItemStack(Material.STONE)
    val itemMeta = itemStack.itemMeta ?: return itemStack
    itemMeta.itemName(Component.text("§aINDEX: §e$index"))
    itemStack.itemMeta = itemMeta
    return itemStack
  }
}