package fr.ping.gamemaker.items.builders.models

import com.google.gson.JsonElement
import fr.ping.gamemaker.items.ItemBuilderContext
import fr.ping.gamemaker.items.templates.models.ItemTemplate
import fr.ping.utils.resources.Resource
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

abstract class ItemBuilder : Resource() {
  abstract fun buildItemLore(
    template : ItemTemplate,
    itemStack : ItemStack,
    key: String,
    value: JsonElement?,
    isKeyInConfig: Boolean = true,
    context: ItemBuilderContext
  ): List<Component>?

  abstract fun buildItemMeta(
    template: ItemTemplate,
    itemStack: ItemStack,
    itemMeta: ItemMeta,
    context: ItemBuilderContext
  ): ItemMeta
}