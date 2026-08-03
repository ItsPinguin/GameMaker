package fr.itspinguin.gamemaker.dialog

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import fr.itspinguin.gamemaker.actions.ActionContext
import fr.itspinguin.gamemaker.actions.ActionManager
import fr.itspinguin.gamemaker.criteria.CriteriaManager
import fr.itspinguin.resourcemanager.Resource
import org.bukkit.entity.Player

data class Dialog(
  @SerializedName("dialog_lines")
  var dialogLines: MutableList<DialogLine> = mutableListOf(DialogLine("Hello world!")),
  var cooldown: Double = -1.0,
  var loops: Boolean = true,

  @SerializedName("should_use_chat")
  var useChat : Boolean? = null,
  @SerializedName("should_use_title")
  var useTitle : Boolean? = null,
  @SerializedName("should_use_action_bar")
  var useActionBar : Boolean? = null
) : Resource() {
  @Transient
  val cooldowns = mutableMapOf<String, Double>()
  @Transient
  val indexes = mutableMapOf<String, Int>()

  fun use(context: JsonObject) {
    val player = context["player"] as? Player ?: return
    val playerName = player.name
    val index = indexes.getOrPut(playerName) { 0 }
    val line = dialogLines.getOrNull(index) ?: return

    if (loops.not() && index >= dialogLines.size - 1) return

    val playerCooldown = cooldowns.getOrPut(playerName) { Double.MIN_VALUE }
    val expectedCooldown = line.cooldown ?: cooldown

    if (playerCooldown + expectedCooldown * 1000 > System.currentTimeMillis()) return
    if (!CriteriaManager.checkCriteria(line.criteria, context)) return
    line.text?.let {
      if ((line.useChat ?: useChat) != false) player.sendMessage(it)
      if ((line.useTitle ?: useTitle) == true) player.sendTitle("", it, 10, 60, 10)
      if ((line.useActionBar ?: useActionBar) == true) player.sendActionBar(it)
    }
    line.actions?.forEach { action ->
      ActionManager.executeAction(action, ActionContext.GenericActionContext(context))
    }
    indexes[playerName] = (index + (line.step ?: 1))
    if (loops)
      indexes[playerName] = indexes[playerName]!! % dialogLines.size
    cooldowns[playerName] = System.currentTimeMillis().toDouble()
  }

  override fun clean() {
    dialogLines.clear()
  }
}