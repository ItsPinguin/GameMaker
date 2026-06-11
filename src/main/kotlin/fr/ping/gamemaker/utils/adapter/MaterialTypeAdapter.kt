package fr.ping.gamemaker.utils.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bukkit.Material

object MaterialTypeAdapter : TypeAdapter<Material>() {
  override fun write(p0: JsonWriter?, p1: Material?) {
    p0?.value(p1?.name?.lowercase() ?: "air")
  }

  override fun read(p0: JsonReader?): Material? {
    return p0?.nextString()?.uppercase()?.let { Material.getMaterial(it) }
  }
}