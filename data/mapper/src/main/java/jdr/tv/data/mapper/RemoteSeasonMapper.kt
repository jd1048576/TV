package jdr.tv.data.mapper

import jdr.tv.data.local.entities.Episode
import jdr.tv.data.local.entities.Season
import jdr.tv.data.remote.entities.RemoteSeason

fun RemoteSeason.toSeason(): Season {
    return Season(airDate, id, name, overview, posterPath, showId, seasonNumber)
}

fun RemoteSeason.toEpisodeList(): List<Episode> {
    return episodesList.map {
        with(it) {
            Episode(
                airDate,
                episodeNumber,
                id,
                name,
                overview,
                this@toEpisodeList.id,
                seasonNumber,
                stillPath,
                voteAverage,
                voteCount
            )
        }
    }
}
