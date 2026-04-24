package fr.ping.gamemaker.addon

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.GameMakerPlugin.Companion.gson
import fr.ping.gamemaker.GameMakerPlugin.Config
import fr.ping.gamemaker.builtin.hook.BuiltinRegistryCreator.triggerRegistry
import fr.ping.gamemaker.resource.Action
import fr.ping.gamemaker.resource.Criterion
import fr.ping.gamemaker.resource.Trigger
import fr.ping.utils.resources.ResourceHandle
import fr.ping.utils.resources.ResourceManager
import org.bukkit.Bukkit
import java.io.File
import java.util.Collections

object AddonManager {
  private val itemLoreBuilderHooks : MutableList<ItemHandlerHook> = Collections.synchronizedList(mutableListOf())
  private val registryCreatorHooks : MutableList<RegistryCreatorHook> = Collections.synchronizedList(mutableListOf())
  private val resourceHandlerHooks : MutableList<ResourceHandlerHook> = Collections.synchronizedList(mutableListOf())
  private val actionExecutorHooks : MutableList<ActionExecutorHook> = Collections.synchronizedList(mutableListOf())
  private val criteriaCheckerHooks : MutableList<CriteriaCheckerHook> = Collections.synchronizedList(mutableListOf())

  fun registerHook(hook: AddonHook) {
    if (hook is RegistryCreatorHook) registryCreatorHooks.add(hook)
    if (hook is ItemHandlerHook) itemLoreBuilderHooks.add(hook)
    if (hook is ResourceHandlerHook) resourceHandlerHooks.add(hook)
    if (hook is ActionExecutorHook) actionExecutorHooks.add(hook)
    if (hook is CriteriaCheckerHook) criteriaCheckerHooks.add(hook)
  }

  @Suppress("UNCHECKED_CAST")
  internal inline fun <reified T : AddonHook> getHooks(): List<T> {
    return when (T::class) {
      RegistryCreatorHook::class -> registryCreatorHooks as List<T>
      ItemHandlerHook::class -> itemLoreBuilderHooks as List<T>
      ResourceHandlerHook::class -> resourceHandlerHooks as List<T>
      ActionExecutorHook::class -> actionExecutorHooks as List<T>
      CriteriaCheckerHook::class -> criteriaCheckerHooks as List<T>
      else -> listOf()
    }
  }

  fun load() {
    val gameMakerPlugin = GameMakerPlugin.getInstance()
    gameMakerPlugin.config = gson.fromJson(File(gameMakerPlugin.dataFolder, "config.json")
      .readText(), Config::class.java) ?: gameMakerPlugin.config
    registryCreatorHooks.forEach { it.createRegistry() }

    Bukkit.getScheduler().runTaskAsynchronously(GameMakerPlugin.getInstance()) { task ->
      resourceHandlerHooks.forEach { it.loadResources() }
      GameMakerPlugin.getInstance().logger.info("Resources loaded asynchronously.")
      //Profiler.requestStatus(StatusRequest(StatusRequestType.HANDLES)).let {
      //  val handleCount = it.handles?.size ?: 0
      //  GameMakerPlugin.getInstance().logger.info("Loaded $handleCount handles.")
      //}
      TriggerManager.reloadTriggers()
    }
  }

  fun save() {
    val gameMakerPlugin = GameMakerPlugin.getInstance()
    File(gameMakerPlugin.dataFolder, "config.json").writeText(gson.toJson(gameMakerPlugin.config))
    resourceHandlerHooks.forEach { it.saveResources() }
  }

  fun unload() {
    resourceHandlerHooks.forEach { it.clearResources() }
  }

  fun reload() {
    load()
    System.gc()
  }

  fun executeAction(action: Action, context: Map<String, Any?>) = actionExecutorHooks.forEach { executor ->
      executor.execute(action, context)
    //GameMakerPlugin.getInstance().logger.info("[TM] Executing action: $action with context: $context, using executor: ${executor.getId()}")
  }

  fun checkCriterion(criterion: Criterion, context: Map<String, Any?>) = criteriaCheckerHooks.none { checker ->
      checker.check(criterion, context).let {
        //GameMakerPlugin.getInstance().logger.info("[TM] passed: $it for checked: ${checker.getId()} with context: $context and criteria: $criterion")
        it
      }.not()
  }

  fun checkCriteria(criterion: List<Criterion>?, context: Map<String, Any?>) : Boolean {
    //GameMakerPlugin.getInstance().logger.info("TMMMMM: criterion: $criterion; context: $context")
    return criterion == null || criterion.none { !checkCriterion(it, context) }
  }

  object TriggerManager {
    val triggers = mutableMapOf<String, MutableList<ResourceHandle<Trigger>>>()

    fun reloadTriggers(triggerEventName: String? = null) {
      //GameMakerPlugin.getInstance().logger.info("[TriggerManager] Reloading all triggers")
      if (triggerEventName != null) triggers.remove(triggerEventName)
      else triggers.clear()
      @Suppress("UNCHECKED_CAST")
      triggerRegistry.listHandles().forEach {
        //GameMakerPlugin.getInstance().logger.info("[TriggerManager] Updating trigger: ${it.resourceName}, has data: ${it.resource?.data}")
        if (triggerEventName == null || triggerEventName == it.resource?.trigger)
          triggers.getOrPut(it.resource?.trigger ?: "dummy") { mutableListOf() }
            .add(it)
      }
      //GameMakerPlugin.getInstance().logger.info("[TriggerManager] Mapped triggers: $triggers")
    }

    fun trigger(triggerName: String, context: Map<String, Any?> = mapOf()) {
      //GameMakerPlugin.getInstance().logger.info("[TriggerManager] Triggered ${triggerName}. Is existent: ${triggers.containsKey(triggerName)}, size: ${triggers[triggerName]?.size}")
      triggers[triggerName]?.forEach { it.resource?.let { trigger ->
        //GameMakerPlugin.getInstance().logger.info("[TM] Checking validity")
        val passes = checkCriteria(trigger.criteria, context)
        //GameMakerPlugin.getInstance().logger.info("[TM] Checked. $passes")
        if (passes) {
          //GameMakerPlugin.getInstance().logger.info("[TM] passed checks")
          trigger.actions?.forEach {
            executeAction(it, context)
          }
        }
      } }
    }
  }
}