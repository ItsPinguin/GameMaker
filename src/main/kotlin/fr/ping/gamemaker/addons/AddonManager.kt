package fr.ping.gamemaker.addons

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.GameMakerPlugin.Companion.gson
import fr.ping.gamemaker.GameMakerPlugin.Companion.triggerRegistry
import fr.ping.gamemaker.GameMakerPlugin.Config
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.triggers.Trigger
import fr.ping.gamemaker.triggers.TriggerManager
import fr.ping.utils.resources.ResourceHandle
import org.bukkit.Bukkit
import java.io.File
import java.util.Collections

object AddonManager {
  private val itemLoreBuilderHooks : MutableList<ItemHandlerHook> = Collections.synchronizedList(mutableListOf())
  private val registryCreatorHooks : MutableList<RegistryCreatorHook> = Collections.synchronizedList(mutableListOf())
  private val resourceHandlerHooks : MutableList<ResourceHandlerHook> = Collections.synchronizedList(mutableListOf())

  fun registerHook(hook: AddonHook) {
    if (hook is RegistryCreatorHook) registryCreatorHooks.add(hook)
    if (hook is ItemHandlerHook) itemLoreBuilderHooks.add(hook)
    if (hook is ResourceHandlerHook) resourceHandlerHooks.add(hook)
  }

  @Suppress("UNCHECKED_CAST")
  internal inline fun <reified T : AddonHook> getHooks(): List<T> {
    return when (T::class) {
      RegistryCreatorHook::class -> registryCreatorHooks as List<T>
      ItemHandlerHook::class -> itemLoreBuilderHooks as List<T>
      ResourceHandlerHook::class -> resourceHandlerHooks as List<T>
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
}