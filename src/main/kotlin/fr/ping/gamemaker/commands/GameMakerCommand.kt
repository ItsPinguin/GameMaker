package fr.ping.gamemaker.commands

import fr.ping.gamemaker.addon.AddonManager
import fr.ping.gamemaker.builtin.hook.BuiltinRegistryCreator.itemRegistry
import fr.ping.gamemaker.builtin.resource.I18n
import fr.ping.gamemaker.resource.ItemTemplate
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
        "give" -> return listOf(*ResourceManager.getRegistry("items")?.listIds()?.toTypedArray() ?: arrayOf())
      }
      return null
    }
    return listOf("help", "reloadall", "giveall", "saveall", "reload", "translate", "give")
  }

  override fun onCommand(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): Boolean {
    when (args.getOrNull(0) ?: "help") {
      "reload", "reloadall" -> {
        sender.sendMessage("Reloading...")
        measureTime {
          ResourceManager.loadAllResources(true, true)
          AddonManager.reload()
        }.let {
          sender.sendMessage("Done reloading. Took ${it.inWholeMilliseconds} ms")
        }
      }
      "saveall" -> {
        sender.sendMessage("Saving...")
        measureTime {
          AddonManager.save()
        }.let {
          sender.sendMessage("Done saving. Took ${it.inWholeMilliseconds} ms")
        }
      }
      "giveall" -> {
        if (sender !is Player) return true
        itemRegistry.listResources().forEach {
          sender.inventory.addItem(it.buildItem())
          sender.sendMessage("Gave ${it.id}")
        }
      }
      "give" -> {
        if (sender !is Player) return true
        val item = args.getOrNull(1) ?: run {
          sender.sendMessage("Usage: /gamemaker give <item>")
          return true
        }
        ResourceManager[item, ItemTemplate::class.java]?.resource?.buildItem()?.let {
          sender.inventory.addItem(it)
          sender.sendMessage("Gave $item")
        }
      }
      //"reload" -> {
      //  val index = args?.getOrNull(1) ?: run {
      //    sender.sendMessage("Usage: /gamemaker reload <resource>")
      //    return true
      //  }
      //  val registry = index.split("/").firstOrNull() ?: return true
      //  val resource = index.removePrefix("$registry/")
//
        //when (registry.split("/").firstOrNull() ?: "") {
        //  "item", "items" -> {
        //    ResourceIO.loadToRegistry(gmkFolder.resolve("items"), gmkFolder.resolve("items").resolve("$resource.json"), gmk.useRegistry<ItemTemplate>("items"))
        //    sender.sendMessage("Reloaded $resource")
        //    return true
        //  }
        //  "lang" -> {
        //    ResourceIO.loadToRegistry(gmkFolder.resolve("lang"), gmkFolder.resolve("lang").resolve("$resource.json"), gmk.useRegistry<I18n>("lang"))
        //    sender.sendMessage("Reloaded $resource")
        //    return true
        //  }
        //  "action", "actions" -> {
        //    ResourceIO.loadToRegistry(gmkFolder.resolve("actions"), gmkFolder.resolve("actions").resolve("$resource.json"), gmk.useRegistry<Action>("actions"))
        //    sender.sendMessage("Reloaded $resource")
        //    return true
        //  }
        //  "trigger", "triggers" -> {
        //    ResourceIO.loadToRegistry(gmkFolder.resolve("triggers"), gmkFolder.resolve("triggers").resolve("$resource.json"), gmk.useRegistry<Trigger>("triggers"))
        //    AddonManager.TriggerManager.reloadTrigger(resource)
        //    sender.sendMessage("Reloaded $resource")
        //    return true
        //  }
        //}

      //}
      "translate" -> {
        sender.sendMessage(i18n?.resource?.translateAndInsert("some.key.to.something", mapOf("thing" to System.currentTimeMillis().toString())).toString())
      }
      else -> {
        sender.sendMessage("reload, save")
      }
    }
    return true
  }
}