package fr.ping.gamemaker.criteria

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.criteria.models.Criterion
import fr.ping.gamemaker.GameMakerPlugin.Companion.criterionCheckerRegistry
import fr.ping.gamemaker.criteria.impl.AllOfCriterionChecker
import fr.ping.gamemaker.criteria.impl.AnyOfCriterionChecker
import fr.ping.gamemaker.criteria.impl.CooldownCriterionChecker
import fr.ping.gamemaker.criteria.impl.EntityTagsCriterionChecker
import fr.ping.gamemaker.criteria.impl.ItemCriterionCheckerHook
import fr.ping.gamemaker.criteria.impl.NoneOfCriterionChecker
import fr.ping.gamemaker.criteria.impl.PlayerHasItemsCriterionChecker

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
    criterionCheckerRegistry.registerResource("any_of", AnyOfCriterionChecker)
    criterionCheckerRegistry.registerResource("all_of", AllOfCriterionChecker)
    criterionCheckerRegistry.registerResource("none_of", NoneOfCriterionChecker)
    criterionCheckerRegistry.registerResource("entity_tags", EntityTagsCriterionChecker)
    criterionCheckerRegistry.registerResource("item", ItemCriterionCheckerHook)
    criterionCheckerRegistry.registerResource("cooldown", CooldownCriterionChecker)
    criterionCheckerRegistry.registerResource("player_has_items", PlayerHasItemsCriterionChecker)
    println(criterionCheckerRegistry.listIds())
    println(criterionCheckerRegistry.listResources())
  }
}