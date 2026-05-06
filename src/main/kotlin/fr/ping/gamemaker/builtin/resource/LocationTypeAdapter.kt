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
    jsonWriter.value(location.world.name)
    jsonWriter.value(location.x)
    jsonWriter.value(location.y)
    jsonWriter.value(location.z)
    if (location.pitch == 0f && location.yaw == 0f) return
    jsonWriter.value(location.yaw)
    jsonWriter.value(location.pitch)
  }

  override fun read(jsonReader: JsonReader?): Location? {
    if (jsonReader == null) return null
    val location = Location(null, 0.0, 0.0, 0.0)
    if (jsonReader.peek() == JsonToken.STRING) {
      location.world = Bukkit.getWorld(jsonReader.nextString()) ?: Bukkit.getWorlds().firstOrNull()
    }
    if (jsonReader.hasNext()) location.x = jsonReader.nextDouble()
    if (jsonReader.hasNext()) location.y = jsonReader.nextDouble()
    if (jsonReader.hasNext()) location.z = jsonReader.nextDouble()
    if (jsonReader.hasNext()) location.yaw = jsonReader.nextDouble().toFloat()
    if (jsonReader.hasNext()) location.pitch = jsonReader.nextDouble().toFloat()
    return location
  }

}