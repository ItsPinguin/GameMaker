package fr.ping.gamemaker.addon

import fr.ping.gamemaker.resource.Action

interface ActionExecutorHook : AddonHook {
  fun execute(action: Action, context: Map<String, Any?>)
}