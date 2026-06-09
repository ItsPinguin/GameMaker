package fr.ping.gamemaker.items

import com.destroystokyo.paper.profile.PlayerProfile
import com.destroystokyo.paper.profile.ProfileProperty
import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.GameMakerPlugin.Companion.itemBuilderRegistry
import fr.ping.gamemaker.i18n.I18nManager
import fr.ping.gamemaker.items.builders.impl.BuiltinItemBuilder
import fr.ping.gamemaker.items.builders.impl.BuiltinItemTemplateItemListBuilder
import fr.ping.gamemaker.items.templates.models.ItemTemplate
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.profile.PlayerTextures
import java.util.UUID

object ItemManager {
  fun buildItem(id: String, context: ItemBuilderContext = ItemBuilderContext.GenericItemBuilderContext()) =
    buildItem(GameMakerPlugin.itemTemplateRegistry.getResource(id), context)

  fun buildItem(template: ItemTemplate?, context: ItemBuilderContext = ItemBuilderContext.GenericItemBuilderContext()) : ItemStack {
    val material = itemBuilderRegistry.listResources().map { it.buildItemMaterial(template?.data ?: mapOf(), context) }.firstOrNull { it != null } ?: return ItemStack(Material.AIR)
    val itemStack = ItemStack(material)
    if (template == null || itemStack.type == Material.AIR) return itemStack
    val itemMeta = itemStack.itemMeta ?: return itemStack
    itemMeta.displayName(I18nManager.translateIfIndicator(template.name))

    val unorderedKeys = template.data.keys.filter { it !in GameMakerPlugin.getInstance().config.itemLoreOrder }
    val builders = itemBuilderRegistry.resourceMap.toMutableMap()
    val lore = mutableListOf<String>()
    GameMakerPlugin.getInstance().config.itemLoreOrder.forEach { propertyName ->
      builders.values.forEach { builder ->
        lore.addAll(builder.resource?.buildItemLore(propertyName, template.data[propertyName], template.data, context) ?: listOf())
      }
    }
    unorderedKeys.forEach {
      builders.values.forEach { builder ->
        lore.addAll(builder.resource?.buildItemLore(it, template.data[it], template.data, context, false) ?: listOf())
      }
    }
    if (lore.size == 1 && lore.firstOrNull() == "")
      lore.clear()
    else
      while (lore.lastOrNull() == "") {
        lore.removeAt(lore.lastIndex)
      }
    itemMeta.lore = lore
    itemMeta.addItemFlags(*ItemFlag.entries.toTypedArray())
    itemMeta.isUnbreakable = true
    if (material == Material.PLAYER_HEAD && template.data["skull_texture"] != null) {
      val skullMeta = itemMeta as SkullMeta
      val base64Texture = template.data["skull_texture"] as? String ?: ""
      skullMeta.playerProfile = Bukkit.getServer().createProfile(UUID.randomUUID()).apply {
        setProperty(ProfileProperty("textures", base64Texture, null))
      }
    }

    itemMeta.persistentDataContainer.set(NamespacedKey("gamemaker", "id"), PersistentDataType.STRING, template.id)

    itemStack.itemMeta = itemMeta

    return itemStack
  }

  fun getItemId(item: ItemStack?) : String? {
    return item?.itemMeta?.persistentDataContainer?.get(NamespacedKey("gamemaker", "id"), PersistentDataType.STRING)
  }

  init {
    itemBuilderRegistry.registerResource("builtin_builder", BuiltinItemBuilder)
    GameMakerPlugin.itemListBuilderRegistry.registerResource("item_templates", BuiltinItemTemplateItemListBuilder)
  }
}