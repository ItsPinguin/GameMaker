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
        MiniMessage.miniMessage().deserialize(inputString)
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