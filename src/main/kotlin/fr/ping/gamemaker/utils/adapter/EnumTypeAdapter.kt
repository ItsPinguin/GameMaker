package fr.ping.gamemaker.utils.adapter
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class EnumTypeAdapter(val enumClazz: Class<out Enum<*>>) : TypeAdapter<Enum<*>>() {

  override fun write(out: JsonWriter?, value: Enum<*>?) {
    if (value == null) {
      out?.nullValue()
    } else {
      out?.value(value.name.lowercase())
    }
  }

  override fun read(reader: JsonReader?): Enum<*>? {
    if (reader == null) return null

    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull()
      return null
    }

    if (reader.peek() != JsonToken.STRING) {
      reader.skipValue()
      return null
    }
    val rawValue = reader.nextString()

    return enumClazz.enumConstants.firstOrNull {
      it.name.equals(rawValue, ignoreCase = true)
    }
  }
}