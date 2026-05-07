package fr.ping.gamemaker.criteria

import fr.ping.gamemaker.criteria.models.Criterion
import fr.ping.utils.resources.Resource

abstract class CriterionChecker : Resource() {
  abstract fun check(criterion: Criterion, context: Map<String, Any?> = mutableMapOf()): Boolean
}