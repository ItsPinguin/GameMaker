package fr.ping.gamemaker.builtin.hook

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.addon.RegistryCreatorHook
import fr.ping.gamemaker.builtin.resource.I18n
import fr.ping.gamemaker.builtin.resource.dialog.Dialog
import fr.ping.gamemaker.resource.Action
import fr.ping.gamemaker.resource.ItemTemplate
import fr.ping.gamemaker.resource.Trigger
import fr.ping.utils.resources.ResourceManager

object BuiltinRegistryCreator : RegistryCreatorHook {
  override fun createRegistry() {
    val gmk = ResourceManager.useNamespace("gamemaker")
    gmk.useRegistry<ItemTemplate>("items")
      .applyScheme(GameMakerPlugin.getInstance().getResource("schemes/item.json"))
    gmk.useRegistry<I18n>("lang")
    gmk.useRegistry<Action>("actions")
    gmk.useRegistry<Trigger>("triggers")
    gmk.useRegistry<Dialog>("dialogs")
  }

  override fun getId(): String = "builtin_registry_creator"

  override fun clean() = Unit
}