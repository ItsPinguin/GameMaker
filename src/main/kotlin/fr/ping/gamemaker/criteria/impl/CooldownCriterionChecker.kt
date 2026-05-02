package fr.ping.gamemaker.criteria.impl

import fr.ping.gamemaker.criteria.CriteriaChecker
import fr.ping.gamemaker.criteria.models.Criterion
import org.bukkit.entity.Player

object CooldownCriterionChecker : CriteriaChecker() {
  val cooldowns = mutableMapOf<String, MutableMap<String, Double>>()

  override fun check(
    criterion: Criterion,
    context: Map<String, Any?>
  ) : Boolean {
    if (criterion.criterion !in listOf("cooldown", "cooldown_action", "cooldown_check")) return true
    val owner = criterion.data["owner"] as? String ?: (context["player"] as? Player)?.name ?: return true
    val group = criterion.data["group"] as? String ?: "global"
    val cooldown = criterion.data["cooldown"] as? Double ?: 0.0
    return (cooldowns.getOrPut(group) { mutableMapOf() }.getOrPut(owner) { Double.MIN_VALUE } < System.currentTimeMillis()).let {
      if (it)
        cooldowns[group]?.set(owner, System.currentTimeMillis() + cooldown * 1000)
      it
    }
  }
}