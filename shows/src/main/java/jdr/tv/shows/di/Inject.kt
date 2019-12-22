package jdr.tv.shows.di

import jdr.tv.data.di.DataComponent
import jdr.tv.shows.ui.ShowsFragment

fun ShowsFragment.inject(component: DataComponent) {
    DaggerShowsComponent.factory()
        .create(this, component)
        .inject(this)
}
