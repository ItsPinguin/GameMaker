package fr.itspinguin.gamemaker.actions.impl

import fr.itspinguin.gamemaker.actions.ActionContext
import fr.itspinguin.gamemaker.actions.ActionExecutor
import fr.itspinguin.gamemaker.actions.ActionManager
import fr.itspinguin.gamemaker.actions.models.Action
import fr.itspinguin.gamemaker.criteria.CriteriaManager
import fr.itspinguin.gamemaker.criteria.models.Criterion
import fr.itspinguin.resourcemanager.ResourceManager

object ActionListAction : ActionExecutor() {
  override fun execute(
    action: Action,
    context: ActionContext
  ) {
    val actions : List<Action> = ResourceManager.parseJson<List<Action>>(action.data["actions"]) ?: listOf()

    val criteria : List<Criterion> = ResourceManager.parseJson<List<Criterion>>(action.data["criteria"]) ?: listOf()

    if (!CriteriaManager.checkCriteria(criteria, context.metadata)) return
    actions.forEach { ActionManager.executeAction(it, context) }
  }
}