package fr.itspinguin.gamemaker

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.itspinguin.gamemaker.actions.ActionExecutor
import fr.itspinguin.gamemaker.actions.models.Action
import fr.itspinguin.gamemaker.commands.GameMakerCommand
import fr.itspinguin.gamemaker.commands.ModernGameMakerCommand
import fr.itspinguin.gamemaker.criteria.CriterionChecker
import fr.itspinguin.gamemaker.dialog.Dialog
import fr.itspinguin.gamemaker.editor.impl.RegistryItemListBuilder
import fr.itspinguin.gamemaker.editor.impl.ResourceItemListBuilder
import fr.itspinguin.gamemaker.i18n.I18n
import fr.itspinguin.gamemaker.i18n.I18nManager
import fr.itspinguin.gamemaker.items.builders.models.ItemBuilder
import fr.itspinguin.gamemaker.items.builders.models.ItemListBuilder
import fr.itspinguin.gamemaker.items.templates.models.ItemTemplate
import fr.itspinguin.gamemaker.listeners.InventoryListener
import fr.itspinguin.gamemaker.listeners.ItemListener
import fr.itspinguin.gamemaker.menus.TestListProvider
import fr.itspinguin.gamemaker.menus.models.MenuTemplate
import fr.itspinguin.gamemaker.notifications.impl.NotifyActionExecutor
import fr.itspinguin.gamemaker.notifications.models.ComposedNotification
import fr.itspinguin.gamemaker.utils.adapter.ComponentTypeAdapter
import fr.itspinguin.gamemaker.utils.adapter.EnumTypeAdapter
import fr.itspinguin.gamemaker.utils.adapter.LocationTypeAdapter
import fr.itspinguin.gamemaker.utils.adapter.SoundTypeAdapter
import fr.itspinguin.gamemaker.utils.adapter.VectorTypeAdapter
import fr.itspinguin.resourcemanager.ResourceManager
import fr.itspinguin.resourcemanager.registry.ReadyRegistry
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.inventory.InventoryType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.JarURLConnection
import java.util.*
import java.util.jar.JarEntry

class GameMakerPlugin : JavaPlugin() {
  override fun onLoad() {
    saveResource("config.yml", false)
    reloadConfig()
    load()

    ResourceManager.addAllResourcePaths(config.getStringList("resources.paths"))
    ResourceManager.registerTypeAdapter(Vector::class.java, VectorTypeAdapter)
    ResourceManager.registerTypeAdapter(Location::class.java, LocationTypeAdapter)
    ResourceManager.registerTypeAdapter(Sound::class.java, SoundTypeAdapter)
    ResourceManager.registerTypeAdapter(Material::class.java, EnumTypeAdapter(Material::class.java))
    ResourceManager.registerTypeAdapter(InventoryType::class.java, EnumTypeAdapter(InventoryType::class.java))
    ResourceManager.registerTypeAdapter(Component::class.java, ComponentTypeAdapter)
    ResourceManager.registerTypeAdapter(org.bukkit.event.block.Action::class.java, EnumTypeAdapter(org.bukkit.event.block.Action::class.java))

    System.gc()
  }

  override fun onEnable() {
    ResourceManager.findSchemeResources(true)
    ResourceManager.loadAllResources(config.getBoolean("resources.check-scheme", false), config.getBoolean("resources.verbose", false))
    I18nManager.compileLoadedI18n()

    itemListBuilderRegistry.registerResource("levels", TestListProvider())
    itemListBuilderRegistry.registerResource("editor.registries", RegistryItemListBuilder)
    itemListBuilderRegistry.registerResource("editor.resource", ResourceItemListBuilder)

    registerCommands()
    registerEvents()
    System.gc()

    ModernGameMakerCommand.register()
  }

  override fun onDisable() {
    ResourceManager.clean()
    //langRegistry.listResources().forEach { i18n ->
    //  i18n.file?.writeText(gson.toJson(i18n))
    //}
  }

  private fun registerCommands() {
    getCommand("gamemaker")?.setExecutor(GameMakerCommand)
  }

  private fun registerEvents() {
    server.pluginManager.registerEvents(InventoryListener, this)
    server.pluginManager.registerEvents(ItemListener, this)
  }

  fun load() {
    copyResourcesDir("editor", dataFolder.resolve("editor"), config.getBoolean("resources.replace-editor-files", false))
    copyResourcesDir("example", dataFolder.resolve("example"), config.getBoolean("resources.replace-example-files", false))
    ResourceManager.addResourcePath(dataFolder.path)
  }

  companion object {
    val itemTemplateRegistry = ReadyRegistry(ItemTemplate::class.java, "item_template")
    val itemBuilderRegistry = ReadyRegistry(ItemBuilder::class.java, "item_builder")
    val actionRegistry = ReadyRegistry(Action::class.java, "action")
    val actionExecutorRegistry = ReadyRegistry(ActionExecutor::class.java, "action_executor")
    val dialogRegistry = ReadyRegistry(Dialog::class.java, "dialog")
    val criterionCheckerRegistry = ReadyRegistry(CriterionChecker::class.java, "criterion_checker")
    val langRegistry = ReadyRegistry(I18n::class.java, "lang")
    val menuTemplateRegistry = ReadyRegistry(MenuTemplate::class.java, "menu_template")
    val itemListBuilderRegistry = ReadyRegistry(ItemListBuilder::class.java, "item_list_provider")
    val notificationRegistry = ReadyRegistry(ComposedNotification::class.java, "notification")

    val gson : Gson = GsonBuilder().setPrettyPrinting().create()

    fun getInstance() : GameMakerPlugin {
      return getPlugin(GameMakerPlugin::class.java)
    }

    init {
      actionExecutorRegistry.registerResource("notify", NotifyActionExecutor())
    }

    fun copyResourcesDir(sourceDir: String, targetDir: File, replace : Boolean = false) {
      if (!targetDir.exists())
        targetDir.mkdirs()
      val resource = javaClass.classLoader.getResource(sourceDir) ?: return

      try {
        val connection: JarURLConnection = resource.openConnection() as JarURLConnection
        connection.jarFile.use { jarFile ->
          val entries: Enumeration<JarEntry> = jarFile.entries()
          while (entries.hasMoreElements()) {
            val entry: JarEntry = entries.nextElement()
            val name: String = entry.getName()

            if (name.startsWith("$sourceDir/") && !entry.isDirectory) {
              val relativePath = name.substring(sourceDir.length)
              val outFile = File(targetDir, relativePath)

              if (!outFile.exists() || replace) {
                outFile.getParentFile().mkdirs()

                jarFile.getInputStream(entry).use { `in` ->
                  FileOutputStream(outFile).use { out ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while ((`in`.read(buffer).also { bytesRead = it }) != -1) {
                      out.write(buffer, 0, bytesRead)
                    }
                  }
                }
              }
            }
          }
        }
      } catch (e: IOException) {
        println("Error copying resources from JAR to directory: $e")
        e.printStackTrace()
      }
    }
  }
}