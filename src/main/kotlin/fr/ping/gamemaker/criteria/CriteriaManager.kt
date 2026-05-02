package fr.ping.gamemaker.criteria

import fr.ping.gamemaker.criteria.models.Criterion
import fr.ping.gamemaker.GameMakerPlugin.Companion.criterionCheckerRegistry
import fr.ping.gamemaker.criteria.impl.CooldownCriterionChecker
import fr.ping.gamemaker.criteria.impl.EntityTagsCriterionChecker
import fr.ping.gamemaker.criteria.impl.ItemCriterionCheckerHook

object CriteriaManager {
  fun checkCriterion(criterion: Criterion, context: Map<String, Any?>?) =
    criterionCheckerRegistry.getResource(criterion.criterion)?.check(criterion, context ?: mutableMapOf()) ?: true

  fun checkCriteria(criteria: List<Criterion>?, context: Map<String, Any?>?) =
    criteria == null || criteria.none { !checkCriterion(it, context) }

  init {
    criterionCheckerRegistry.registerResource("entity_tags", EntityTagsCriterionChecker)
    criterionCheckerRegistry.registerResource("item", ItemCriterionCheckerHook)
    criterionCheckerRegistry.registerResource("cooldown", CooldownCriterionChecker)
  }
}