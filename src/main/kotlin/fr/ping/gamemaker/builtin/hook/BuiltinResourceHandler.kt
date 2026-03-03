package fr.ping.gamemaker.builtin.hook

import fr.ping.gamemaker.GameMakerPlugin.Companion.gmk
import fr.ping.gamemaker.GameMakerPlugin.Companion.gmkFolder
import fr.ping.gamemaker.addon.ResourceHandlerHook
import fr.ping.gamemaker.builtin.resource.I18n
import fr.ping.gamemaker.builtin.resource.dialog.Dialog
import fr.ping.gamemaker.resource.Action
import fr.ping.gamemaker.resource.ItemTemplate
import fr.ping.gamemaker.resource.Trigger
import fr.ping.utils.resources.ResourceIO

object BuiltinResourceHandler : ResourceHandlerHook {
  override fun loadResources() {
    ResourceIO.loadAllToRegistry(gmkFolder.resolve("items"),
      gmk.useRegistry<ItemTemplate>("items"))
    ResourceIO.loadAllToRegistry(gmkFolder.resolve("lang"),
      gmk.useRegistry<I18n>("lang"))
    ResourceIO.loadAllToRegistry(gmkFolder.resolve("actions"),
      gmk.useRegistry<Action>("actions"))
    ResourceIO.loadAllToRegistry(gmkFolder.resolve("triggers"),
      gmk.useRegistry<Trigger>("triggers"))
    ResourceIO.loadAllToRegistry(gmkFolder.resolve("dialogs"),
      gmk.useRegistry<Dialog>("dialogs"))
  }

  override fun saveResources() {
    //ResourceIO.saveAllToFile(gmkFolder.resolve("items"),
    //  gmk.useRegistry<ItemTemplate>("items"))
    ResourceIO.saveAllToFile(gmkFolder.resolve("lang"),
      gmk.useRegistry<I18n>("lang"))
    //ResourceIO.saveAllToFile(gmkFolder.resolve("actions"),
    //  gmk.useRegistry<Action>("actions"))
    //ResourceIO.saveAllToFile(gmkFolder.resolve("triggers"),
    //  gmk.useRegistry<Trigger>("triggers"))
    //ResourceIO.saveAllToFile(gmkFolder.resolve("dialogs"),
    //  gmk.useRegistry<Dialog>("dialogs"))
  }

  override fun clearResources() {
    gmk.dropRegistry("items")
    gmk.dropRegistry("lang")
    gmk.dropRegistry("actions")
    gmk.dropRegistry("triggers")
    gmk.dropRegistry("dialogs")
  }

  override fun getId(): String = "builtin_resource_handler"

  override fun clean() = clearResources()
}