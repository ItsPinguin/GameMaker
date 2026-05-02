package fr.ping.gamemaker.items.templates.models

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.utils.resources.Resource
import org.bukkit.Material

@JsonAdapter(ItemTemplate.Adapter::class)
open class ItemTemplate(
  val data: MutableMap<String, Any> = mutableMapOf<String, Any>()
) : Resource() {

  var material: Material
    get() = Material.getMaterial((data["material"] as? String)?.uppercase() ?: "AIR") ?: Material.AIR
    set(value) { data["material"] = value.name.lowercase() }
  var name: String
    get() = data["name"] as? String ?: "Unnamed item"
    set(value) { data["name"] = value }

  class Adapter : TypeAdapter<ItemTemplate>() {
    override fun write(out: JsonWriter?, value: ItemTemplate?) {
      out?.jsonValue(GameMakerPlugin.gson.toJson(value?.data))
    }

    override fun read(`in`: JsonReader?): ItemTemplate {
      `in` ?: return ItemTemplate()
      if (`in`.peek() == JsonToken.BEGIN_OBJECT) {
        val itemTemplate = ItemTemplate(GameMakerPlugin.gson.fromJson(`in`, MutableMap::class.java))
        return itemTemplate
      }
      return ItemTemplate()
    }
  }
}