package fr.itspinguin.gamemaker.items.builders.impl

import fr.itspinguin.gamemaker.GameMakerPlugin
import fr.itspinguin.gamemaker.items.ItemBuilderContext
import fr.itspinguin.gamemaker.items.ItemManager
import fr.itspinguin.gamemaker.items.builders.models.ItemListBuilder
import fr.itspinguin.gamemaker.menus.models.MenuInstance
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

object BuiltinItemTemplateItemListBuilder : ItemListBuilder() {
  override fun getItem(index: Int, context: ItemBuilderContext): ItemStack {
    return ItemManager.buildItem(GameMakerPlugin.itemTemplateRegistry.listResources().getOrNull(index)).let { itemStack ->
      val itemMeta = itemStack.itemMeta ?: return itemStack
      itemMeta.lore = (itemMeta.lore ?: mutableListOf<String>()).apply {
        add(" ")
        add("§e§lLEFT CLICK §7to give 1")
        add("§e§lRIGHT CLICK §7to give ${itemStack.maxStackSize}")
      }
      itemStack.itemMeta = itemMeta
      itemStack
    }
  }

  override fun getListSize(context: ItemBuilderContext): Int {
    return GameMakerPlugin.itemTemplateRegistry.listResources().size
  }

  override fun onClick(index: Int, event: InventoryClickEvent, menuInstance: MenuInstance) {
    val template = GameMakerPlugin.itemTemplateRegistry.listResources().getOrNull(index) ?: return
    val itemStack = ItemManager.buildItem(template)
    if (event.click == ClickType.RIGHT) itemStack.amount = itemStack.maxStackSize
    event.whoClicked.inventory.addItem(itemStack)
  }
}