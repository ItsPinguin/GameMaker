package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.items.ItemManager
import fr.ping.gamemaker.items.templates.models.ItemTemplate
import fr.ping.utils.resources.ResourceManager
import org.bukkit.entity.Player

object GiveItemAction: ActionExecutor() {
  val lang by lazy { GameMakerPlugin.langRegistry.resourceMap["en_US"] }

  override fun execute(
    action: Action,
    context: Map<String, Any?>
  ) {
    if (action.action !in listOf("give", "give_item", "item")) return
    val itemId = action.data["item"] as? String ?: return
    val player = context["player"] as? Player ?: return
    @Suppress("DEPRECATION")
    ResourceManager[itemId, ItemTemplate::class.java]?.resource?.let { item ->
      val itemStack = ItemManager.buildItem(item)
      player.inventory.addItem(itemStack)
      if ((action.data["should_message"] as? Boolean == true))
        player.sendMessage(lang?.resource?.translateAndInsert("give_item.give_message", mapOf(
          "item_name" to itemStack.itemMeta.displayName
        )).toString())
    }
  }
}