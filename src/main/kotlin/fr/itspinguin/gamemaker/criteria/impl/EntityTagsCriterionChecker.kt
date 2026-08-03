package fr.itspinguin.gamemaker.criteria.impl

import com.google.gson.JsonObject
import fr.itspinguin.gamemaker.criteria.CriterionChecker
import fr.itspinguin.gamemaker.criteria.models.Criterion
import org.bukkit.entity.Entity

object
EntityTagsCriterionChecker : CriterionChecker(){
  override fun check(
    criterion: Criterion,
    context: JsonObject
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