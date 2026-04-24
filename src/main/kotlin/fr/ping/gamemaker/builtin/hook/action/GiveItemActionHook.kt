package fr.ping.gamemaker.builtin.hook.action

import fr.ping.gamemaker.addon.ActionExecutorHook
import fr.ping.gamemaker.builtin.hook.BuiltinRegistryCreator
import fr.ping.gamemaker.resource.Action
import fr.ping.gamemaker.resource.ItemTemplate
import fr.ping.utils.resources.ResourceManager
import org.bukkit.entity.Player

object GiveItemActionHook: ActionExecutorHook {
  val lang by lazy { BuiltinRegistryCreator.langRegistry.resourceMap["en_US"] }

  override fun execute(
    action: Action,
    context: Map<String, Any?>
  ) {
    if (action.action !in listOf("give", "give_item", "item")) return
    val itemId = action.data["item"] as? String ?: return
    val player = context["player"] as? Player ?: return
    @Suppress("DEPRECATION")
    ResourceManager[itemId, ItemTemplate::class.java]?.resource?.let { item ->
      val itemStack = item.buildItem()
      player.inventory.addItem(itemStack)
      if ((action.data["should_message"] as? Boolean == true))
        player.sendMessage(lang?.resource?.translateAndInsert("give_item.give_message", mapOf(
          "item_name" to itemStack.itemMeta.displayName
        )).toString())
    }
  }
}