package fr.itspinguin.gamemaker.editor.impl

import fr.itspinguin.gamemaker.i18n.I18nManager
import fr.itspinguin.gamemaker.items.ItemBuilderContext
import fr.itspinguin.gamemaker.items.builders.models.ItemListBuilder
import fr.itspinguin.gamemaker.items.templates.models.ItemTemplate
import fr.itspinguin.resourcemanager.ResourceManager
import net.kyori.adventure.text.Component
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
        is ItemTemplate -> Material.valueOf(resource.material)
        else -> try {
          Material.valueOf(I18nManager.getString("editor.icons.${context.menuInstance.data["registry"]}.${resource.id}"))
        } catch (_ : Exception) {
          Material.GRAY_DYE
        }
      }
    )
    val itemMeta = itemStack.itemMeta ?: return itemStack



    itemMeta.itemName(Component.text("§a" + resource.id))
    itemMeta.lore(listOf(
      Component.text("§7Id: §f${resource.id}"),
      Component.text("§7File: §f${resource.file}")
      ))

    itemStack.itemMeta = itemMeta
    return itemStack
  }
}