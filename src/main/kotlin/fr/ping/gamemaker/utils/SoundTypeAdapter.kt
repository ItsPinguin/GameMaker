package fr.ping.gamemaker.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.Sound

object SoundTypeAdapter : TypeAdapter<Sound> () {
  override fun write(writer: JsonWriter, sound: Sound?) {
    if (sound == null) {
      writer.nullValue()
      return
    }
    @Suppress("REMOVAL")
    writer.value(sound.key.toString())
  }

  override fun read(reader: JsonReader): Sound? {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull()
      return null
    }

    val rawValue = reader.nextString()

    val key = NamespacedKey.fromString(rawValue.lowercase().replace("minecraft:", "").replace("_", "."))
    if (key != null) {
      val registeredSound = Registry.SOUNDS.get(key)
      Registry.MATERIAL
      if (registeredSound != null) return registeredSound
    }

    return Sound.ENTITY_EXPERIENCE_ORB_PICKUP
  }
}