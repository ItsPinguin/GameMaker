package fr.ping.gamemaker.builtin.resource

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.utils.resources.Resource
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

@JsonAdapter(I18n.Adapter::class)
class I18n(
  private val translations: MutableMap<String, Any?> = mutableMapOf<String, Any?>()
) : Resource {
  fun translate(key: String) : String {
    val path = key.lowercase().split(".")
    var current = translations
    for (i in path.subList(0, path.lastIndex)) {
      current = current.getOrPut(i) { mutableMapOf<String, Any?>() } as? MutableMap<String, Any?>
        ?: throw IllegalStateException("Invalid path: $key. Referred to non-map at $i")
    }
    return current.getOrPut(path.last()) { path.last() + " {*}" }.toString()
  }

  fun translateAndInsert(key: String, data: Map<String, Any?>) = insertVariables(translate(key), data)
    .replace("\\n", "\n").replace("{*}", "{${data.entries.joinToString(", ") { it.key + ": " + it.value }}}")

  fun insertVariables(message: String = "", args : Map<*, *> = mapOf<String, Any?>()): String {
    var message = message
    for (i in args.entries) {
      message = message.replace("{${i.key}}", i.value.toString())
    }
    return message
  }

  override fun getId(): String = ""

  override fun setId(id: String) = Unit

  override fun clean() {
    translations.clear()
  }

  class Adapter : TypeAdapter<I18n>() {
    override fun write(out: JsonWriter?, value: I18n?) {
      out?.jsonValue(GameMakerPlugin.Companion.gson.toJson(value?.translations))
    }

    override fun read(`in`: JsonReader?): I18n? {
      if (`in`?.peek() == JsonToken.BEGIN_OBJECT) {
        return I18n(GameMakerPlugin.Companion.gson.fromJson(`in`, MutableMap::class.java))
      }
      return I18n()
    }
  }
}