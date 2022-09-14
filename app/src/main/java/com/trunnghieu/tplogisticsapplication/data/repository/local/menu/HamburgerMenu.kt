package com.trunnghieu.tplogisticsapplication.data.repository.local.menu

data class HamburgerMenu(
    var type_id: Int,
    var title: String,
    var sub_menu: List<SubMenu>? = listOf()
) {

    data class SubMenu(
        var type_id: Int,
        var icon: Int,
        var title: String
    )

}