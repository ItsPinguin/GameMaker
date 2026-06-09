package fr.ping.gamemaker.utils

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

object ComponentTypeAdapter : TypeAdapter<Component>() {
  override fun write(writer: JsonWriter?, component: Component?) {
    if (writer == null || component == null) return
    writer.value(GsonComponentSerializer.gson().serialize(component))
  }

  override fun read(reader: JsonReader?): Component {
    if (reader == null) return Component.empty()
    val token = reader.peek()

    if (token == JsonToken.BEGIN_ARRAY) {
      reader.beginArray()
      val components: MutableList<Component> = mutableListOf()
      while (reader.hasNext() && reader.peek() != JsonToken.END_ARRAY)
        components.add(readSingleComponent(reader))
      reader.endArray()

      return Component.join(JoinConfiguration.separator(Component.newline()), components)
    }

    if (token == JsonToken.BEGIN_OBJECT || token == JsonToken.STRING)
      return readSingleComponent(reader)

    reader.skipValue()
    return Component.empty()
  }

  fun readSingleComponent(reader: JsonReader): Component {
    val token = reader.peek()

    if (token == JsonToken.STRING) {
      val inputString = reader.nextString()
      return try {
        return MiniMessage.miniMessage().deserialize(inputString
          .replace("§4", "<dark_red>", true)
          .replace("§c", "<red>", true)
          .replace("§6", "<gold>", true)
          .replace("§e", "<yellow>", true)
          .replace("§2", "<dark_green>", true)
          .replace("§a", "<green>", true)
          .replace("§b", "<aqua>", true)
          .replace("§3", "<dark_aqua>", true)
          .replace("§1", "<dark_blue>", true)
          .replace("§9", "<blue>", true)
          .replace("§d", "<light_purple>", true)
          .replace("§5", "<dark_purple>", true)
          .replace("§f", "<white>", true)
          .replace("§7", "<gray>", true)
          .replace("§8", "<dark_gray>", true)
          .replace("§0", "<black>", true)
          .replace("§k", "<obfuscated>", true)
          .replace("§l", "<bold>", true)
          .replace("§m", "<strikethrough>", true)
          .replace("§n", "<underline>", true)
          .replace("§o", "<italic>", true)
          .replace("§r", "<reset>", true)
        )
      } catch (_: Exception) {
        Component.empty()
      }
    }

    if (token == JsonToken.BEGIN_OBJECT) {
      val jsonElement: JsonElement = JsonParser.parseReader(reader)
      return GsonComponentSerializer.gson().deserializeFromTree(jsonElement)
    }

    reader.skipValue()
    return Component.empty()
  }
}