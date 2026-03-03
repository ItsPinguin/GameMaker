package fr.ping.gamemaker.builtin.hook.criteria

import fr.ping.gamemaker.addon.CriteriaCheckerHook
import fr.ping.gamemaker.resource.Criterion
import org.bukkit.entity.Player

object CooldownCriterionChecker : CriteriaCheckerHook {
  val cooldowns = mutableMapOf<String, MutableMap<String, Double>>()

  override fun check(
    criterion: Criterion,
    context: Map<String, Any?>
  ) : Boolean {
    //GameMakerPlugin.getInstance().logger.info("[CooldownChecker] received criteria: $criterion and context: $context")
    if (criterion.criterion !in listOf("cooldown", "cooldown_action", "cooldown_check")) return true
    val owner = criterion.data["owner"] as? String ?: (context["player"] as? Player)?.name ?: return true
    val group = criterion.data["group"] as? String ?: "global"
    val cooldown = criterion.data["cooldown"] as? Double ?: 0.0
    //GameMakerPlugin.getInstance().logger.info("[CooldownChecker] received criteria: $criterion and context: $context, extracted: owner=$owner, group=$group, cooldown=$cooldown")
    return (cooldowns.getOrPut(group) { mutableMapOf() }.getOrPut(owner) { Double.MIN_VALUE } < System.currentTimeMillis()).let {
      if (it)
        cooldowns[group]?.set(owner, System.currentTimeMillis() + cooldown * 1000)
      //GameMakerPlugin.getInstance().logger.info("[CooldownChecker] cooldown check result: $it, cooldowns: $cooldowns")
      it
    }
  }

  override fun getId(): String {
    return "cooldown"
  }

  override fun clean() {
    cooldowns.forEach { (_, map) -> map.clear() }
    cooldowns.clear()
  }
}