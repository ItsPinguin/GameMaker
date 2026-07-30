package fr.ping.gamemaker.actions

import fr.ping.gamemaker.actions.models.Action
import fr.ping.utils.resources.Resource

abstract class ActionExecutor : Resource() {
  /**
   * Executes a specified action within the given context.
   *
   * @param action The action to be executed. Represents a task or behavior defined by a set of triggers, criteria, or other properties.
   * @param context The context in which the action is executed. Provides metadata or additional information required to perform the action.
   */
  abstract fun execute(action: Action, context: ActionContext)
}