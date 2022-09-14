package com.trunnghieu.tplogisticsapplication.data.repository.local.menu

enum class MenuType(val typeId: Int) {
    JOB_HISTORY(1),
    DOWNLOAD_STATEMENT(2),
    ACCOUNT_SETTINGS(3),
    LANGUAGE(4),
    DASHBOARD(5),
    SCAN_QR(6)
}

enum class SubMenuType(val typeId: Int) {
    ENGLISH(1),
    VIETNAMESE(2),
}