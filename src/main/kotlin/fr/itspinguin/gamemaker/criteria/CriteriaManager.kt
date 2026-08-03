package fr.itspinguin.gamemaker.criteria

import com.google.gson.JsonObject
import fr.itspinguin.gamemaker.GameMakerPlugin.Companion.criterionCheckerRegistry
import fr.itspinguin.gamemaker.criteria.impl.*
import fr.itspinguin.gamemaker.criteria.models.Criterion

object CriteriaManager {
  fun checkCriterion(criterion: Criterion, context: JsonObject = JsonObject()) : Boolean {
    if (criterion.criterion == "always_true") return true
    if (criterion.criterion == "always_false") return false
    return criterionCheckerRegistry.getResource(criterion.criterion)?.check(criterion, context) ?: true
  }
  fun checkCriteria(criteria: List<Criterion>?, context: JsonObject = JsonObject()) : Boolean {
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