package fr.ping.gamemaker.actions

import fr.ping.gamemaker.actions.models.Action
import fr.ping.utils.resources.Resource

abstract class ActionExecutor : Resource() {
  abstract fun execute(action: Action, context: Map<String, Any?>)
}