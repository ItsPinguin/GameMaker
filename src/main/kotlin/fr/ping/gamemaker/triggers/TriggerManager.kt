package fr.ping.gamemaker.triggers

import fr.ping.gamemaker.GameMakerPlugin.Companion.triggerRegistry
import fr.ping.gamemaker.actions.ActionManager
import fr.ping.gamemaker.criteria.CriteriaManager
import fr.ping.utils.resources.ResourceHandle
import kotlin.collections.forEach

object TriggerManager {
  val triggers = mutableMapOf<String, MutableList<ResourceHandle<Trigger>>>()

  fun reloadTriggers(triggerEventName: String? = null) {
    if (triggerEventName != null) triggers.remove(triggerEventName)
    else triggers.clear()
    @Suppress("UNCHECKED_CAST")
    triggerRegistry.listHandles().forEach {
      if (triggerEventName == null || triggerEventName == it.resource?.trigger)
        triggers.getOrPut(it.resource?.trigger ?: "dummy") { mutableListOf() }
          .add(it)
    }
  }

  fun trigger(triggerName: String, context: Map<String, Any?> = mapOf()) {
    triggers[triggerName]?.forEach { it.resource?.let { trigger ->
      val passes = CriteriaManager.checkCriteria(trigger.criteria, context)
      if (passes) {
        trigger.actions?.forEach { action ->
          ActionManager.executeAction(action, context)
        }
      }
    } }
  }
}