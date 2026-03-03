package fr.ping.gamemaker

import fr.ping.gamemaker.addon.AddonManager
import fr.ping.gamemaker.builtin.BuiltinAddon
import fr.ping.gamemaker.builtin.events.TriggerEventListener
import fr.ping.gamemaker.builtin.hook.BuiltinItemBuilder
import fr.ping.gamemaker.commands.GameMakerCommand
import fr.ping.utils.resources.ResourceManager
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class GameMakerPlugin : JavaPlugin() {
  var config: Config = Config()

  override fun onLoad() {
    dataFolder.mkdirs()
    if (!File(dataFolder, "config.json").exists())
      File(dataFolder, "config.json").writeText(gson.toJson(config))
    else
      config = gson.fromJson<Config>(File(dataFolder, "config.json").readText(), Config::class.java) ?: config

    BuiltinAddon.registerHooks()

    System.gc()
  }

  override fun onEnable() {
    AddonManager.load()

    registerCommands()
    registerEvents()
    System.gc()
  }

  override fun onDisable() {
    AddonManager.save()
    AddonManager.unload()
    ResourceManager.clean()
  }

  private fun registerCommands() {
    getCommand("gamemaker")?.setExecutor(GameMakerCommand)
  }

  private fun registerEvents() {
    server.pluginManager.registerEvents(TriggerEventListener, this)
  }

  data class Config(
    var builtins: Builtins = Builtins(),
    @SerializedName("item_lore_order")
    var itemLoreOrder: List<String> = listOf("attributes", "lore", "enchants")
  ) {
    data class Builtins(
      var itemBuilder : BuiltinItemBuilder.Config = BuiltinItemBuilder.Config()
    )
  }

  companion object {
    val gmk by lazy { ResourceManager.useNamespace("gamemaker") }
    val gmkFolder by lazy { getResourceFolder().resolve("gamemaker") }

    val gson = GsonBuilder().setPrettyPrinting().create()
    private val pathRegex = Regex("([a-zA-Z]*):?([a-zA-Z0-9]*)/?([a-zA-Z0-9|/]*)")

    fun getInstance() : GameMakerPlugin {
      return getPlugin(GameMakerPlugin::class.java)
    }

    fun getResourceFolder() : File {
      return getInstance().dataFolder.resolve("data")
    }

    fun inferPath(path: String, defaultRegistry: String) : List<String> {
      pathRegex.matchEntire(path)?.let { matchResult ->
        when (matchResult.groups.size) {
          1 -> return listOf("gamemaker", defaultRegistry, matchResult.groupValues[1])
          2 -> {
            if (matchResult.groupValues[2].isBlank()) {
              return listOf(matchResult.groupValues[1], defaultRegistry, matchResult.groupValues[3])
            }
            return listOf("gamemaker", matchResult.groupValues[2], matchResult.groupValues[3])
          }
          3 -> return listOf(matchResult.groupValues[1], matchResult.groupValues[2], matchResult.groupValues[3])
          else -> throw IllegalArgumentException(
            "Invalid path: $path"
          )
        }
      }
      throw IllegalArgumentException("Invalid path: $path")
    }
  }
}
