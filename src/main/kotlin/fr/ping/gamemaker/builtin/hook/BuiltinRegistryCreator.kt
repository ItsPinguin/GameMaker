package fr.ping.gamemaker.builtin.hook

import fr.ping.fr.ping.utils.resources.ReadyRegistry
import fr.ping.gamemaker.addon.RegistryCreatorHook
import fr.ping.gamemaker.builtin.resource.I18n
import fr.ping.gamemaker.builtin.resource.dialog.Dialog
import fr.ping.gamemaker.resource.Action
import fr.ping.gamemaker.resource.ItemTemplate
import fr.ping.gamemaker.resource.Trigger
import fr.ping.utils.resources.ResourceManager

object BuiltinRegistryCreator : RegistryCreatorHook {
  val itemRegistry = ReadyRegistry(ItemTemplate::class.java)
  val langRegistry = ReadyRegistry(I18n::class.java)
  val actionRegistry = ReadyRegistry(Action::class.java)
  val triggerRegistry = ReadyRegistry(Trigger::class.java)
  val dialogRegistry = ReadyRegistry(Dialog::class.java)

  override fun createRegistry() {
    ResourceManager["item"] = itemRegistry
    ResourceManager["lang"] = langRegistry
    ResourceManager["action"] = actionRegistry
    ResourceManager["trigger"] = triggerRegistry
    ResourceManager["dialog"] = dialogRegistry
  }
}