package fr.ping.gamemaker.criteria.impl

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.criteria.CriteriaChecker
import fr.ping.gamemaker.criteria.models.Criterion
import org.bukkit.entity.Entity

object EntityTagsCriterionChecker : CriteriaChecker(){
  override fun check(
    criterion: Criterion,
    context: Map<String, Any?>
  ): Boolean {
    GameMakerPlugin.getInstance().logger.info("[TAGChecker] received criteria: $criterion and context: $context")
    if (criterion.criterion !in listOf("entity_tag", "entity_tags")) return true
    GameMakerPlugin.getInstance().logger.info("[TAGChecker] ok")
    val entity = (context["entity"] as? Entity?) ?: return false
    GameMakerPlugin.getInstance().logger.info("[TAGChecker] entity: $entity, tags: ${criterion.data["tags"]}, entity tags: ${entity.scoreboardTags}")
    val tags = criterion.data["tags"] as? List<*> ?: return false
    for (tag in tags) {
      if (!entity.scoreboardTags.contains(tag))
        return false
    }
    return true
  }
}