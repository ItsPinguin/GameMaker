package fr.ping.gamemaker.actions

import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.GameMakerPlugin.Companion.actionExecutorRegistry
import fr.ping.gamemaker.actions.impl.CommandAction
import fr.ping.gamemaker.actions.impl.DialogAction
import fr.ping.gamemaker.actions.impl.GiveItemsAction
import fr.ping.gamemaker.actions.impl.MessagePlayer
import fr.ping.gamemaker.actions.impl.TakeItemsAction
import fr.ping.gamemaker.actions.impl.TradeItemsAction

object ActionManager {
  fun executeAction(action: Action, context: Map<String, Any?> = mapOf()) =
    actionExecutorRegistry.listResources()
      .forEach { executor -> executor.execute(action, context) }

  init {
    actionExecutorRegistry.registerResource("message_player", MessagePlayer)
    actionExecutorRegistry.registerResource("dialog", DialogAction)
    actionExecutorRegistry.registerResource("give_items", GiveItemsAction)
    actionExecutorRegistry.registerResource("take_items", TakeItemsAction)
    actionExecutorRegistry.registerResource("command", CommandAction)
    actionExecutorRegistry.registerResource("trade_items", TradeItemsAction)
  }
}