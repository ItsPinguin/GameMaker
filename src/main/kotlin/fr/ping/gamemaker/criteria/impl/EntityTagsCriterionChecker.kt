package fr.ping.gamemaker.criteria.impl

import fr.ping.gamemaker.criteria.CriterionChecker
import fr.ping.gamemaker.criteria.models.Criterion
import org.bukkit.entity.Entity

object EntityTagsCriterionChecker : CriterionChecker(){
  override fun check(
    criterion: Criterion,
    context: Map<String, Any?>
  ): Boolean {
    if (criterion.criterion !in listOf("entity_tag", "entity_tags")) return true
    val entity = (context["entity"] as? Entity?) ?: return false
    val tags = criterion.data["tags"] as? List<*> ?: return false
    for (tag in tags) {
      if (!entity.scoreboardTags.contains(tag))
        return false
    }
    return true
  }
}