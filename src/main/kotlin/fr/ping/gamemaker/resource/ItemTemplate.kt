package fr.ping.gamemaker.resource

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.addon.AddonManager
import fr.ping.gamemaker.addon.ItemHandlerHook
import fr.ping.utils.resources.Resource
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

@JsonAdapter(ItemTemplate.Adapter::class)
open class ItemTemplate(
  val data: MutableMap<String, Any> = mutableMapOf<String, Any>()
) : Resource {

  var material: Material
    get() = Material.getMaterial((data["material"] as? String)?.uppercase() ?: "AIR") ?: Material.AIR
    set(value) { data["material"] = value.name.lowercase() }
  var name: String
    get() = data["name"] as? String ?: "Unnamed item"
    set(value) { data["name"] = value }

  fun buildItem() : ItemStack {
    val itemStack = ItemStack(material)
    if (itemStack.type == Material.AIR) return itemStack
    val itemMeta = itemStack.itemMeta ?: return itemStack
    itemMeta.setDisplayName(name)

    val actualLore : MutableList<String> = mutableListOf()
    GameMakerPlugin.getInstance().config.itemLoreOrder.forEach { loreKey ->
      val valueToRender = data[loreKey]
      var renderedValue : List<String>? = null
      AddonManager.getHooks<ItemHandlerHook>().forEach { hook ->
        hook.buildItemLore(loreKey, valueToRender, data.toMap())?.let {
          renderedValue = it
          return@forEach
        }
      }
      if (renderedValue != null) actualLore.addAll(renderedValue)
    }
    if (actualLore.size == 1 && actualLore.firstOrNull() == "")
      actualLore.clear()
    else
      while (actualLore.lastOrNull() == "") {
        actualLore.removeAt(actualLore.lastIndex)
      }
    itemMeta.lore = actualLore
    itemMeta.addItemFlags(*ItemFlag.entries.toTypedArray())

    itemMeta.persistentDataContainer.set(NamespacedKey("gamemaker", "item"), PersistentDataType.STRING, getId())

    itemStack.itemMeta = itemMeta
    return itemStack
  }

  override fun getId(): String {
    return data["id"] as? String ?: "invalid_id"
  }

  override fun setId(id: String) {
    data["id"] = id
  }

  override fun clean() {

  }

  class Adapter : TypeAdapter<ItemTemplate>() {
    override fun write(out: JsonWriter?, value: ItemTemplate?) {
      out?.jsonValue(GameMakerPlugin.gson.toJson(value?.data))
    }

    override fun read(`in`: JsonReader?): ItemTemplate {
      `in` ?: return ItemTemplate()
      if (`in`.peek() == JsonToken.BEGIN_OBJECT) {
        val itemTemplate = ItemTemplate(GameMakerPlugin.Companion.gson.fromJson(`in`, MutableMap::class.java))
        return itemTemplate
      }
      return ItemTemplate()
    }
  }
}