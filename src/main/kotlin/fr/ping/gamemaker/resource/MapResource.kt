package fr.ping.gamemaker.resource

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.utils.resources.Resource
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

@JsonAdapter(MapResource.Adapter::class)
open class MapResource(
  var data: MutableMap<String, Any?> = mutableMapOf<String, Any?>()
): Resource() {

  override fun clean() {
    data.clear()
  }

  class Adapter : GeneralAdapter<MapResource>(MapResource::class.java)

  open class GeneralAdapter <T : MapResource> (val clazz: Class<T>) : TypeAdapter<T>() {
    override fun write(out: JsonWriter?, value: T?) {
      out?.jsonValue(GameMakerPlugin.Companion.gson.toJson(value?.data))
    }

    override fun read(`in`: JsonReader?): T? {
      `in` ?: return clazz.newInstance()
      if (`in`.peek() == JsonToken.BEGIN_OBJECT) {
        val mapResource = clazz.newInstance()
        mapResource.data = GameMakerPlugin.gson.fromJson(`in`, MutableMap::class.java)
        return mapResource
      }
      return clazz.newInstance()
    }
  }
}