package fr.ping.gamemaker.editor.impl

import fr.ping.gamemaker.i18n.I18nManager
import fr.ping.gamemaker.items.ItemBuilderContext
import fr.ping.gamemaker.items.builders.models.ItemListBuilder
import fr.ping.gamemaker.items.templates.models.ItemTemplate
import fr.ping.gamemaker.resource.MapResource
import fr.ping.utils.resources.ResourceManager
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ResourceItemListBuilder : ItemListBuilder() {
  override fun getListSize(context: ItemBuilderContext): Int {
    if (context !is ItemBuilderContext.MenuSlotItemBuilderContext) return 0
    return ResourceManager.getRegistryMap()[context.menuInstance.data["registry"] ?: return 0]?.listHandles()?.size ?: 0
  }

  override fun getItem(index: Int, context: ItemBuilderContext): ItemStack? {
    if (context !is ItemBuilderContext.MenuSlotItemBuilderContext) return null
    val registry = ResourceManager.getRegistryMap()[context.menuInstance.data["registry"] ?: return null] ?: return null
    if (index !in registry.listHandles().indices) return null
    val resource = registry.listResources().getOrNull(index) ?: return null
    val itemStack = ItemStack(
      when (resource) {
        is ItemTemplate -> resource.material
        else -> try {
          println(I18nManager["editor.icons.${context.menuInstance.data["registry"]}.${resource.id}"])
          Material.valueOf(I18nManager["editor.icons.${context.menuInstance.data["registry"]}.${resource.id}"].toString())
        } catch (_ : Exception) {
          Material.GRAY_DYE
        }
      }
    )
    val itemMeta = itemStack.itemMeta ?: return itemStack



    itemMeta.setItemName("§a" + resource.id)
    itemMeta.lore = listOf(
      "§7Id: §f${resource.id}",
      "§7File: §f${resource.file}"
    )

    itemStack.itemMeta = itemMeta
    return itemStack
  }
}