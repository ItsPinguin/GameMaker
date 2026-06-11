package fr.ping.gamemaker.i18n

data class I18nConfig(
  var defaultLanguage : String = "ENGLISH",
  val languageFallbacks : MutableMap<String, String> = mutableMapOf(
    "FRENCH" to "ENGLISH",
  )
) {
}