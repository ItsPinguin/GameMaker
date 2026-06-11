package fr.ping.gamemaker.utils.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bukkit.event.inventory.InventoryType

object InventoryTypeTypeAdapter : TypeAdapter<InventoryType>() {
  override fun write(p0: JsonWriter?, p1: InventoryType?) {
    p0?.value(p1?.name?.lowercase())
  }

  override fun read(p0: JsonReader?): InventoryType {
    return p0?.nextString()?.uppercase()?.let { try {
      InventoryType.valueOf(it)
    } catch (_ : Exception) {
      InventoryType.CHEST
    }
    } ?: InventoryType.CHEST
  }
}