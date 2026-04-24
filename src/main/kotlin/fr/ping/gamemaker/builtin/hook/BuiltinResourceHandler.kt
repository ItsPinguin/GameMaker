package fr.ping.gamemaker.builtin.hook

import fr.ping.gamemaker.GameMakerPlugin.Companion.gmkFolder
import fr.ping.gamemaker.addon.ResourceHandlerHook
import fr.ping.gamemaker.builtin.resource.I18n
import fr.ping.gamemaker.builtin.resource.dialog.Dialog
import fr.ping.gamemaker.resource.Action
import fr.ping.gamemaker.resource.ItemTemplate
import fr.ping.gamemaker.resource.Trigger

object BuiltinResourceHandler : ResourceHandlerHook {
  override fun loadResources() {
  }

  override fun saveResources() {
    //ResourceIO.saveAllToFile(gmkFolder.resolve("items"),
    //  gmk.useRegistry<ItemTemplate>("items"))
    //ResourceIO.saveAllToFile(gmkFolder.resolve("actions"),
    //  gmk.useRegistry<Action>("actions"))
    //ResourceIO.saveAllToFile(gmkFolder.resolve("triggers"),
    //  gmk.useRegistry<Trigger>("triggers"))
    //ResourceIO.saveAllToFile(gmkFolder.resolve("dialogs"),
    //  gmk.useRegistry<Dialog>("dialogs"))
  }

  override fun clearResources() {

  }

}