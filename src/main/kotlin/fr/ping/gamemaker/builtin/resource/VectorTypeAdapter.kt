package fr.ping.gamemaker.builtin.resource

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bukkit.util.Vector

object VectorTypeAdapter : TypeAdapter<Vector>() {
  override fun write(jsonWriter: JsonWriter?, vector: Vector?) {
    jsonWriter?.beginArray()
    jsonWriter?.value(vector?.x ?: 0.0)
    jsonWriter?.value(vector?.y ?: 0.0)
    jsonWriter?.value(vector?.z ?: 0.0)
    jsonWriter?.endArray()
  }

  override fun read(jsonReader: JsonReader?): Vector {
    val vector = Vector()
    if (jsonReader == null) return vector
    jsonReader.beginArray()
    vector.x = if (jsonReader.hasNext()) jsonReader.nextDouble() else 0.0
    vector.y = if (jsonReader.hasNext()) jsonReader.nextDouble() else 0.0
    vector.z = if (jsonReader.hasNext()) jsonReader.nextDouble() else 0.0
    jsonReader.endArray()
    return vector
  }
}