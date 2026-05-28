package fr.ping.gamemaker.i18n

import fr.ping.gamemaker.GameMakerPlugin
import org.bukkit.entity.Player
import kotlin.toString

object I18nManager {
  val languages : MutableMap<String, MutableMap<String, Any?>> = mutableMapOf()
  var defaultLanguage : String = "ENGLISH"
  val languageFallbacks : MutableMap<String, String> = mutableMapOf(
    "FRENCH" to "ENGLISH",
  )

  operator fun get(key: String, vararg args: Any?) : String = get(defaultLanguage, key, *args)

  operator fun get(locale: String, key: String, vararg args: Any?) : String {
    val translations = languages[locale] ?: languages[languageFallbacks[locale]] ?: languages[defaultLanguage] ?: return key
    return translateAndInsert(translations, key, *args)
  }

  operator fun get(player: Player, key: String, vararg args: Any?) : String =
    get(player.locale, key, *args)

  fun compileLoadedI18n() {
    GameMakerPlugin.langRegistry.listResources().forEach { i18n ->
      languages.getOrPut(i18n.locale) { mutableMapOf() }.apply {
        i18n.translations.forEach { (key, value) -> put(key, value) }
      }
    }
  }

  private fun translate(translations : MutableMap<String, Any?>, key: String) : String {
    val path = key.lowercase().split(".")
    var current = translations
    for (i in path.subList(0, path.lastIndex)) {
      current = current.getOrPut(i) { mutableMapOf<String, Any?>() } as? MutableMap<String, Any?>
        ?: throw IllegalStateException("Invalid path: $key. Referred to non-map at $i")
    }
    return current.getOrPut(path.last()) { key }
      .let { any -> if (any is Collection<*>) any.joinToString("\n") { it.toString() } else any.toString() }
  }

  private fun translateAndInsert(translations : MutableMap<String, Any?>, key: String, vararg args: Any?) : String {
    return String.format(translate(translations, key), *args)
  }
}