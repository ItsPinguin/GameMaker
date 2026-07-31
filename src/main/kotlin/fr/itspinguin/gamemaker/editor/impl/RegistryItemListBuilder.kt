package fr.itspinguin.gamemaker.editor.impl

import com.google.gson.JsonObject
import fr.itspinguin.gamemaker.actions.ActionContext
import fr.itspinguin.gamemaker.actions.impl.OpenMenuAction
import fr.itspinguin.gamemaker.actions.models.Action
import fr.itspinguin.gamemaker.i18n.I18nManager
import fr.itspinguin.gamemaker.items.ItemBuilderContext
import fr.itspinguin.gamemaker.items.builders.models.ItemListBuilder
import fr.itspinguin.gamemaker.menus.models.MenuInstance
import fr.itspinguin.resourcemanager.ResourceManager
import net.kyori.adventure.text.Component
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
      Material.valueOf(I18nManager["editor.registry_icons.${registryPair.key}"].toString())
    } catch (_ : Exception) {
      Material.CHEST })
    val itemMeta = itemStack.itemMeta ?: return itemStack

    itemMeta.itemName(Component.text("§a" + registry.type.simpleName))
    itemMeta.lore(listOf(
      Component.text("§7Id: §f${registryPair.key}"),
      Component.text("§7Resource count: §f${registry.listHandles().size}")
    ))

    itemStack.itemMeta = itemMeta
    return itemStack
  }

  override fun onClick(index: Int, event: InventoryClickEvent, menuInstance: MenuInstance) {
    if (index !in 0 until ResourceManager.getRegistryMap().size) return
    val registryPair = ResourceManager.getRegistryMap().entries.toList().getOrNull(index) ?: return
    OpenMenuAction.execute(
      Action().apply {
        this.data = JsonObject().apply {
          addProperty("registry", registryPair.key)
          addProperty("menu", "editor.resource")
        }
      },
      ActionContext.MenuClickActionContext(
        event.whoClicked as Player,
        menuInstance,
        event
      )
    )
  }
}