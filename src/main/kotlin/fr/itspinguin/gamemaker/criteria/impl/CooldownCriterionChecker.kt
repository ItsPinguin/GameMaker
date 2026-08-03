package fr.itspinguin.gamemaker.criteria.impl

import com.google.gson.JsonObject
import fr.itspinguin.gamemaker.criteria.CriterionChecker
import fr.itspinguin.gamemaker.criteria.models.Criterion
import org.bukkit.entity.Player

object CooldownCriterionChecker : CriterionChecker() {
  val cooldowns = mutableMapOf<String, MutableMap<String, Double>>()

  override fun check(
    criterion: Criterion,
    context: JsonObject
  ) : Boolean {
    if (criterion.criterion !in listOf("cooldown", "cooldown_action", "cooldown_check")) return true
    val owner = criterion.data["owner"]?.asString ?: (context["player"] as? Player)?.name ?: return true
    val group = criterion.data["group"]?.asString ?: "global"
    val cooldown = criterion.data["cooldown"]?.asDouble ?: 0.0
    return (cooldowns.getOrPut(group) { mutableMapOf() }.getOrPut(owner) { Double.MIN_VALUE } < System.currentTimeMillis()).let {
      if (it)
        cooldowns[group]?.set(owner, System.currentTimeMillis() + cooldown * 1000)
      it
    }
  }
}