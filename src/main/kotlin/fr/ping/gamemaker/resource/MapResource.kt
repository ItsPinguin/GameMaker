package fr.ping.gamemaker.resource

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.utils.resources.Resource
import fr.ping.utils.resources.ResourceManager

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
      clazz.declaredFields.forEach { field ->
        val names = field.getDeclaredAnnotation(SerializedName::class.java)?.let {
          mutableListOf(it.value).apply { addAll(it.alternate) }
        }
        val chosenName = names?.firstOrNull { value?.data[it] != null } ?: names?.firstOrNull() ?: field.name
        value?.data[chosenName] = field.get(value)
      }
      out?.jsonValue(GameMakerPlugin.gson.toJson(value?.data))
    }

    override fun read(`in`: JsonReader?): T? {
      `in` ?: return clazz.newInstance()
      if (`in`.peek() == JsonToken.BEGIN_OBJECT) {
        val mapResource = clazz.newInstance()
        mapResource.data = GameMakerPlugin.gson.fromJson(`in`, MutableMap::class.java)
        mapResource.id = mapResource.data["id"] as? String ?: mapResource.id
        mapResource.type = mapResource.data["type"] as? String ?: mapResource.type
        clazz.declaredFields.forEach { field ->
          field.isAccessible = true
          val names : List<String> = field.getDeclaredAnnotation(SerializedName::class.java)?.let {
            mutableListOf(it.value).apply { addAll(it.alternate) }
          } ?: listOf()
          val value = names.firstNotNullOfOrNull { mapResource.data[it] } ?: mapResource.data[field.name] ?: return@forEach
          field.set(mapResource, ResourceManager.parseAny(value, field.genericType))
        }
        return mapResource
      }
      return clazz.newInstance()
    }
  }
}