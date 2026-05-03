package fr.ping.gamemaker.commands

import fr.ping.gamemaker.GameMakerPlugin.Companion.criterionCheckerRegistry
import fr.ping.gamemaker.GameMakerPlugin.Companion.itemTemplateRegistry
import fr.ping.gamemaker.GameMakerPlugin.Companion.menuTemplateRegistry
import fr.ping.gamemaker.builtin.resource.I18n
import fr.ping.gamemaker.criteria.impl.CooldownCriterionChecker
import fr.ping.gamemaker.criteria.impl.EntityTagsCriterionChecker
import fr.ping.gamemaker.criteria.impl.ItemCriterionCheckerHook
import fr.ping.gamemaker.criteria.impl.PlayerHasItemCriterionChecker
import fr.ping.gamemaker.items.ItemManager
import fr.ping.gamemaker.menus.MenuManager
import fr.ping.utils.resources.ResourceManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import kotlin.time.measureTime

object GameMakerCommand : TabExecutor {
  private val i18n by lazy { ResourceManager.getHandle("items/en_US", I18n::class.java) }
  override fun onTabComplete(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): List<String?>? {
    if ((args.size) > 1) {
      when (args.getOrNull(0) ?: "") {
        "give" -> return listOf(*itemTemplateRegistry.listIds().toTypedArray())
        "menu" -> return listOf(*menuTemplateRegistry.listIds().toTypedArray())
      }
      return null
    }
    return listOf("help", "giveall", "reload", "translate", "give", "menu", "clear_cached_menus", "reload_checkers")
  }

  override fun onCommand(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): Boolean {
    when (args.getOrNull(0) ?: "help") {
      "reload" -> {
        sender.sendMessage("§fReloading...")
        measureTime {
          ResourceManager.loadAllResources(true, true)
        }.let {
          sender.sendMessage("§7Done reloading. Took §f${it.inWholeMilliseconds} ms")
        }
      }
      "giveall" -> {
        if (sender !is Player) return true
        itemTemplateRegistry.listResources().forEach {
          sender.inventory.addItem(ItemManager.buildItem(it))
        }
        sender.sendMessage("§7Gave §f${itemTemplateRegistry.listResources().size} items")
      }
      "give" -> {
        if (sender !is Player) return true
        val item = args.getOrNull(1) ?: run {
          sender.sendMessage("§7Usage: §f/gamemaker give <item>")
          return true
        }
        sender.inventory.addItem(ItemManager.buildItem(item))
        sender.sendMessage("§7Gave §f$item")
      }
      "clear_cached_menus" -> {
        MenuManager.menus.clear()
        sender.sendMessage("§7Cleared all cached menus")
      }
      "menu" -> {
        if (sender !is Player) return true
        val menu = args.getOrNull(1) ?: run {
          sender.sendMessage("§7Usage: §f/gamemaker menu <menu>")
          return true
        }
        MenuManager.open(sender, menu)
      }
      "translate" -> {
        sender.sendMessage(i18n?.resource?.translateAndInsert("some.key.to.something", mapOf("thing" to System.currentTimeMillis().toString())).toString())
      }
      "reload_checkers" -> {
        criterionCheckerRegistry.registerResource("entity_tags", EntityTagsCriterionChecker)
        criterionCheckerRegistry.registerResource("item", ItemCriterionCheckerHook)
        criterionCheckerRegistry.registerResource("cooldown", CooldownCriterionChecker)
        criterionCheckerRegistry.registerResource("player_has_items", PlayerHasItemCriterionChecker)
      }
      else -> {
        sender.sendMessage("reload")
      }
    }
    return true
  }
}