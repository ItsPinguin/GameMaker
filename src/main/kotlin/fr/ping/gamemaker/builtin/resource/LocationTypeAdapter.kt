package fr.ping.gamemaker.builtin.resource

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.bukkit.Bukkit
import org.bukkit.Location

object LocationTypeAdapter : TypeAdapter<Location>() {
  override fun write(jsonWriter: JsonWriter?, location: Location?) {
    if (jsonWriter == null || location == null) return
    jsonWriter.beginArray()
    jsonWriter.value(location.world.name)
    jsonWriter.value(location.x)
    jsonWriter.value(location.y)
    jsonWriter.value(location.z)
    if (location.pitch == 0f && location.yaw == 0f) {
      jsonWriter.endArray()
      return
    }
    jsonWriter.value(location.yaw)
    jsonWriter.value(location.pitch)
    jsonWriter.endArray()
  }

  override fun read(jsonReader: JsonReader?): Location? {
    if (jsonReader == null) return null
    val location = Location(null, 0.0, 0.0, 0.0)
    if (jsonReader.peek() == JsonToken.NULL) return null
    if (jsonReader.peek() != JsonToken.BEGIN_ARRAY) return null
    jsonReader.beginArray()
    if (jsonReader.peek() == JsonToken.STRING) {
      location.world = Bukkit.getWorld(jsonReader.nextString()) ?: Bukkit.getWorlds().firstOrNull()
    }
    if (jsonReader.hasNext() && jsonReader.peek() == JsonToken.NUMBER) location.x = jsonReader.nextDouble()
    if (jsonReader.hasNext() && jsonReader.peek() == JsonToken.NUMBER) location.y = jsonReader.nextDouble()
    if (jsonReader.hasNext() && jsonReader.peek() == JsonToken.NUMBER) location.z = jsonReader.nextDouble()
    if (jsonReader.hasNext() && jsonReader.peek() == JsonToken.NUMBER) location.yaw = jsonReader.nextDouble().toFloat()
    if (jsonReader.hasNext() && jsonReader.peek() == JsonToken.NUMBER) location.pitch = jsonReader.nextDouble().toFloat()
    jsonReader.endArray()
    return location
  }

}