package fr.ping.gamemaker.editor.impl

import fr.ping.gamemaker.actions.ActionContext
import fr.ping.gamemaker.actions.impl.OpenMenuAction
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.i18n.I18nManager
import fr.ping.gamemaker.items.ItemBuilderContext
import fr.ping.gamemaker.items.builders.models.ItemListBuilder
import fr.ping.gamemaker.menus.models.MenuInstance
import fr.ping.utils.resources.ResourceManager
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

object RegistryItemListBuilder : ItemListBuilder() {
  override fun getListSize(context: ItemBuilderContext): Int {
    return ResourceManager.getRegistryMap().size
  }

  override fun getItem(index: Int, context: ItemBuilderContext): ItemStack? {
    if (context !is ItemBuilderContext.MenuSlotItemBuilderContext) return null
    if (index !in 0 until ResourceManager.getRegistryMap().size) return null
    val registryPair = ResourceManager.getRegistryMap().entries.toList()[index]
    val registry = registryPair.value
    val itemStack = ItemStack(try {
      Material.valueOf(I18nManager["editor.registry_icons.${registryPair.key}"])
    } catch (_ : Exception) {
      Material.CHEST })
    val itemMeta = itemStack.itemMeta ?: return itemStack

    itemMeta.setItemName("§a" + registry.type.simpleName)
    itemMeta.lore = listOf(
      "§7Id: §f${registryPair.key}",
      "§7Resource count: §f${registry.listHandles().size}"
    )

    itemStack.itemMeta = itemMeta
    return itemStack
  }

  override fun onClick(index: Int, event: InventoryClickEvent, menuInstance: MenuInstance) {
    if (index !in 0 until ResourceManager.getRegistryMap().size) return
    val registryPair = ResourceManager.getRegistryMap().entries.toList().getOrNull(index) ?: return
    OpenMenuAction.execute(
      Action().apply {
        this.data = mutableMapOf(
          "registry" to registryPair.key,
          "menu" to "editor/resource"
        )
      },
      ActionContext.MenuClickActionContext(
        event.whoClicked as Player,
        menuInstance,
        event
      )
    )
  }
}