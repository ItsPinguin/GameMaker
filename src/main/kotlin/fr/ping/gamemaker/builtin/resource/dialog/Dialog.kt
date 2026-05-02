package fr.ping.gamemaker.builtin.resource.dialog

import fr.ping.gamemaker.addons.AddonManager
import fr.ping.utils.resources.Resource
import com.google.gson.annotations.SerializedName
import fr.ping.gamemaker.actions.ActionManager
import fr.ping.gamemaker.criteria.CriteriaManager
import org.bukkit.entity.Player

data class Dialog(
  @Transient
  var _id: String = "",
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

  fun use(context: Map<String, Any?> = mapOf()) {
    var player = context["player"] as? Player ?: return
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
      ActionManager.executeAction(action, context)
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