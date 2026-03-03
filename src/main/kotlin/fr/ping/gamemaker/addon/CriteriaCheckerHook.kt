package fr.ping.gamemaker.addon

import fr.ping.gamemaker.resource.Criterion

interface CriteriaCheckerHook : AddonHook {
  fun check(criterion: Criterion, context: Map<String, Any?>): Boolean
}