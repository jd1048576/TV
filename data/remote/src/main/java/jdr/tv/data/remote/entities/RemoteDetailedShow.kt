package jdr.tv.data.remote.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant
import jdr.tv.common.extensions.INSTANT_ZERO

@JsonClass(generateAdapter = true)
class RemoteDetailedShow(
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "content_ratings")
    val contentRatings: ContentRatings,
    @Json(name = "created_by")
    val createdByList: List<RemoteCreatedBy>,
    @Json(name = "credits")
    val credits: Credits,
    @Json(name = "episode_run_time")
    val episodeRunTimeList: List<Int>,
    @Json(name = "external_ids")
    val externalIds: RemoteExternalIds,
    @Json(name = "first_air_date")
    val firstAirDate: Instant = INSTANT_ZERO,
    @Json(name = "genres")
    val genreList: List<RemoteGenre>,
    @Json(name = "homepage")
    val homepage: String,
    @Json(name = "id")
    val id: Long,
    @Json(name = "in_production")
    val inProduction: Boolean,
    @Json(name = "languages")
    val languageList: List<String>,
    @Json(name = "last_air_date")
    val lastAirDate: Instant = INSTANT_ZERO,
    @Json(name = "name")
    val name: String,
    @Json(name = "networks")
    val networkList: List<RemoteNetwork>,
    @Json(name = "number_of_episodes")
    val numberOfEpisodes: Int,
    @Json(name = "number_of_seasons")
    val numberOfSeasons: Int,
    @Json(name = "origin_country")
    val originCountryList: List<String>,
    @Json(name = "original_language")
    val originalLanguage: String,
    @Json(name = "original_name")
    val originalName: String,
    @Json(name = "overview")
    val overview: String,
    @Json(name = "popularity")
    val popularity: Double,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "production_companies")
    val productionCompanyList: List<RemoteProductionCompany>,
    @Json(name = "recommendations")
    val recommendations: RemoteShowList,
    @Json(name = "similar")
    val similar: RemoteShowList,
    @Json(name = "status")
    val status: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "videos")
    val videos: Videos,
    @Json(name = "vote_average")
    val voteAverage: Double,
    @Json(name = "vote_count")
    val voteCount: Int
) {

    @JsonClass(generateAdapter = true)
    class ContentRatings(
        @Json(name = "results")
        val contentRatingList: List<RemoteContentRating>
    )

    @JsonClass(generateAdapter = true)
    class Credits(
        @Json(name = "cast")
        val castList: List<RemoteCast>,
        @Json(name = "crew")
        val crewList: List<RemoteCrew>
    )

    @JsonClass(generateAdapter = true)
    class Videos(
        @Json(name = "results")
        val videoList: List<RemoteVideo>
    )
}
