package fr.ping.gamemaker.builtin.resource

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bukkit.util.Vector

class VectorTypeAdapter : TypeAdapter<Vector>() {
  override fun write(jsonWriter: JsonWriter?, vector: Vector?) {
    jsonWriter?.beginArray()
    jsonWriter?.value(vector?.x ?: 0.0)
    jsonWriter?.value(vector?.y ?: 0.0)
    jsonWriter?.value(vector?.z ?: 0.0)
    jsonWriter?.endArray()
  }

  override fun read(jsonReader: JsonReader?): Vector {
    val vector = Vector()
    jsonReader?.beginArray()
    vector.x = jsonReader?.nextDouble() ?: jsonReader?.nextInt()?.toDouble() ?: 0.0
    vector.y = jsonReader?.nextDouble() ?: jsonReader?.nextInt()?.toDouble() ?: 0.0
    vector.z = jsonReader?.nextDouble() ?: jsonReader?.nextInt()?.toDouble() ?: 0.0
    jsonReader?.endArray()
    return vector
  }
}