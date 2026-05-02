package fr.ping.gamemaker.menus.models

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import fr.ping.utils.resources.Resource

@JsonAdapter(SlotFillInfo.Adapter::class)
class SlotFillInfo : Resource() {
  var fillType: SlotFillType = SlotFillType.INDIVIDUAL
  var slot : Int = 0
  var fromSlot : Int = 0
  var toSlot : Int = 0
  var row : Int = 0
  fun getSlots() : List<Int> {
    return when (fillType) {
      SlotFillType.INDIVIDUAL -> listOf(slot)
      SlotFillType.ROW -> IntRange(row * 9, row * 9 + 8).toList()
      SlotFillType.RANGE -> IntRange(fromSlot, toSlot).toList()
      SlotFillType.FILL -> IntRange(0, 54).toList()
    }
  }

  enum class SlotFillType {
    INDIVIDUAL,
    ROW,
    RANGE,
    FILL
  }

  class Adapter : TypeAdapter<SlotFillInfo>() {
    override fun write(jsonWriter: JsonWriter?, slotFillInfo: SlotFillInfo?) {
      when (slotFillInfo?.fillType) {
        SlotFillType.INDIVIDUAL -> {
          jsonWriter?.value(slotFillInfo.slot)
        }
        SlotFillType.ROW -> {
          jsonWriter?.value("r:${slotFillInfo.toSlot}")
        }
        SlotFillType.RANGE -> {
          jsonWriter?.value("${slotFillInfo.fromSlot}:${slotFillInfo.toSlot}")
        }
        SlotFillType.FILL -> {
          jsonWriter?.value("fill")
        }
        else -> {}
      }
    }

    override fun read(jsonReader: JsonReader?): SlotFillInfo? {
      if (jsonReader == null) return null
      when (jsonReader.peek()) {
        JsonToken.NUMBER -> {
          return SlotFillInfo().apply {
            slot = jsonReader.nextInt()
          }
        }
        JsonToken.STRING -> {
          val value = jsonReader.nextString()
          if (value.startsWith("r")) {
            return SlotFillInfo().apply {
              row = value.replace("r:", "").replace("row ", "").toIntOrNull() ?: 0
              fillType = SlotFillType.ROW
            }
          }
          if (value == "fill") return SlotFillInfo().apply {
            fillType = SlotFillType.FILL
          }
          if (value.contains(":")) {
            val split = value.split(":")
            return SlotFillInfo().apply {
              fromSlot = split[0].toIntOrNull() ?: 0
              toSlot = split[1].toIntOrNull() ?: 0
              if (fromSlot > toSlot) {
                val tmp = fromSlot
                fromSlot = toSlot
                toSlot = tmp
              }
              fillType = SlotFillType.RANGE
            }
          }
          jsonReader.skipValue()
          return null
        }
        else -> {
          jsonReader.skipValue()
          return null
        }
      }
    }

  }
}