package fr.ping.gamemaker.menus.models

data class PageState(
  var pageSize : Int = 9,
  var page : Int = 0,
  var total : Int = 0
) {
  fun getOffset() = page * pageSize
}
