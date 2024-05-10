package org.d3if3126.assessment2.navigation

import org.d3if3126.assessment2.ui.screen.KEY_ID_DANUS

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_DANUS}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}