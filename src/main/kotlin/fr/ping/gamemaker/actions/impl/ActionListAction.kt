package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.actions.ActionContext
import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.ActionManager
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.criteria.CriteriaManager
import fr.ping.gamemaker.criteria.models.Criterion
import fr.ping.utils.resources.ResourceManager

object ActionListAction : ActionExecutor() {
  override fun execute(
    action: Action,
    context: ActionContext
  ) {
    val actions : List<Action> =
      if ((action.data["actions"] as? List<*>)?.all { it is Action } == false)
        ResourceManager.parseAny<List<Action>>(action.data["actions"]) ?: listOf()
      else
        action.data["actions"] as List<Action>

    val criteria : List<Criterion> =
      if ((action.data["criteria"] as? List<*>)?.all { it is Map<*, *> } == false)
        ResourceManager.parseAny<List<Criterion>>(action.data["criteria"]) ?: listOf()
      else
        action.data["criteria"] as List<Criterion>

    if (!CriteriaManager.checkCriteria(criteria, context.metadata)) return
    actions.forEach { ActionManager.executeAction(it, context) }
  }
}