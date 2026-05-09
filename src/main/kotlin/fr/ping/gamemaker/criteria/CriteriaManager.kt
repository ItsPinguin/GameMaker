package fr.ping.gamemaker.criteria

import fr.ping.gamemaker.GameMakerPlugin.Companion.criterionCheckerRegistry
import fr.ping.gamemaker.criteria.impl.*
import fr.ping.gamemaker.criteria.models.Criterion

object CriteriaManager {
  fun checkCriterion(criterion: Criterion, context: Map<String, Any?>?) : Boolean {
    if (criterion.criterion == "always_true") return true
    if (criterion.criterion == "always_false") return false
    return criterionCheckerRegistry.getResource(criterion.criterion)?.check(criterion, context ?: mutableMapOf()) ?: true
  }
  fun checkCriteria(criteria: List<Criterion>?, context: Map<String, Any?>?) : Boolean {
    return criteria == null || criteria.all { checkCriterion(it, context) }
  }

  init {
    criterionCheckerRegistry.registerResource("any_of", AnyOfCriterionChecker)
    criterionCheckerRegistry.registerResource("all_of", AllOfCriterionChecker)
    criterionCheckerRegistry.registerResource("none_of", NoneOfCriterionChecker)
    criterionCheckerRegistry.registerResource("entity_tags", EntityTagsCriterionChecker)
    criterionCheckerRegistry.registerResource("item", ItemCriterionCheckerHook)
    criterionCheckerRegistry.registerResource("cooldown", CooldownCriterionChecker)
    criterionCheckerRegistry.registerResource("player_has_items", PlayerHasItemsCriterionChecker)
  }
}