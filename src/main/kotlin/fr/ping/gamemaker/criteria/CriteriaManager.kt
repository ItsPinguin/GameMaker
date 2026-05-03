package fr.ping.gamemaker.criteria

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.GameMakerPlugin.Companion.actionExecutorRegistry
import fr.ping.gamemaker.criteria.models.Criterion
import fr.ping.gamemaker.GameMakerPlugin.Companion.criterionCheckerRegistry
import fr.ping.gamemaker.actions.impl.MessagePlayer
import fr.ping.gamemaker.criteria.impl.CooldownCriterionChecker
import fr.ping.gamemaker.criteria.impl.EntityTagsCriterionChecker
import fr.ping.gamemaker.criteria.impl.ItemCriterionCheckerHook
import fr.ping.gamemaker.criteria.impl.PlayerHasItemCriterionChecker

object CriteriaManager {
  fun checkCriterion(criterion: Criterion, context: Map<String, Any?>?) : Boolean {
    GameMakerPlugin.getInstance().logger.info("Checking criterion: $criterion, context: $context")
    if (criterion.criterion == "always_true") return true
    if (criterion.criterion == "always_false") return false
    println(criterionCheckerRegistry.listIds())
    println(criterionCheckerRegistry.listResources())
    return criterionCheckerRegistry.getResource(criterion.criterion)?.check(criterion, context ?: mutableMapOf()) ?: true
  }
  fun checkCriteria(criteria: List<Criterion>?, context: Map<String, Any?>?) : Boolean {
    return criteria == null || criteria.all { checkCriterion(it, context) }
  }

  init {
    println(criterionCheckerRegistry.listIds())
    criterionCheckerRegistry.registerResource("entity_tags", EntityTagsCriterionChecker)
    criterionCheckerRegistry.registerResource("item", ItemCriterionCheckerHook)
    criterionCheckerRegistry.registerResource("cooldown", CooldownCriterionChecker)
    criterionCheckerRegistry.registerResource("player_has_items", PlayerHasItemCriterionChecker)
    println(criterionCheckerRegistry.listIds())
    println(criterionCheckerRegistry.listResources())
  }
}