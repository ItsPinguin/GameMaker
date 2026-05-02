package fr.ping.gamemaker.builtin.resource.dialog

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.builtin.resource.dialog.DialogLine.Adapter
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.criteria.models.Criterion
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

@JsonAdapter(Adapter::class)
data class DialogLine(
  var text: String? = null,
  var actions: List<Action>? = null,
  var criteria: List<Criterion>? = null,
  var cooldown: Double? = null,
  var step: Int? = null,

  @SerializedName("should_use_chat")
  var useChat : Boolean? = null,
  @SerializedName("should_use_title")
  var useTitle: Boolean? = null,
  @SerializedName("should_use_action_bar")
  var useActionBar: Boolean? = null
) {
  class Adapter : TypeAdapter<DialogLine>() {
    override fun write(
      out: JsonWriter?,
      value: DialogLine?
    ) {
      if (value == null) return
      val data = mutableMapOf<String, Any?>()
      value.text.let { data["text"] = it }
      value.actions?.let { data["actions"] = it }
      value.criteria?.let { data["criteria"] = it }
      value.cooldown?.let { data["cooldown"] = it }
      value.step?.let { data["step"] = it }
      value.useTitle?.let { data["should_use_title"] = it }
      value.useActionBar?.let { data["should_use_action_bar"] = it }
      if (data.size == 1 && data.containsKey("text"))
        out?.value(value.text)
      else
        out?.jsonValue(GameMakerPlugin.Companion.gson.toJson(data))
    }

    override fun read(`in`: JsonReader?): DialogLine? {
      if (`in` == null) return DialogLine()
      when (`in`.peek()) {
        JsonToken.STRING -> {
          return DialogLine(text = `in`.nextString())
        }
        JsonToken.BEGIN_OBJECT -> {
          val data = GameMakerPlugin.Companion.gson.fromJson<Map<*, *>>(`in`, Map::class.java)
          return DialogLine(
            text = data["text"] as? String ?: "",
            actions = (data["actions"] as? List<*>)?.map { Action().apply { this.data =
              it as Map<*, *> as MutableMap<String, Any?>
            } } ?: listOf(),
            criteria = (data["criteria"] as? List<*>)?.map { Criterion().apply {
              this.data = it as Map<*, *> as MutableMap<String, Any?>
            } } ?: listOf(),
            cooldown = data["cooldown"] as? Double,
            step = data["step"] as? Int,
            useTitle = data["should_use_title"] as? Boolean,
            useActionBar = data["should_use_action_bar"] as? Boolean
          )
        }
        else -> return DialogLine()
      }
    }
  }
}