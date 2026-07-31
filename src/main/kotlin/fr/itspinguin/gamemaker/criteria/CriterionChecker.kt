package fr.itspinguin.gamemaker.criteria

import fr.itspinguin.gamemaker.criteria.models.Criterion
import fr.itspinguin.resourcemanager.Resource

abstract class CriterionChecker : Resource() {
  /**
   * Evaluates the given criterion based on the provided context.
   *
   * @param criterion The criterion object containing the conditions to evaluate.
   * @param context An optional map providing additional data or state for the evaluation.
   * @return True if the criterion is satisfied based on the context; otherwise, false.
   */
  abstract fun check(criterion: Criterion, context: Map<String, Any?> = mutableMapOf()): Boolean
}