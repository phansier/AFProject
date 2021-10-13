package com.tromian.game.afproject.data.repository


import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.tromian.game.afproject.AppConstants.NETWORK_PAGE_SIZE
import com.tromian.game.afproject.appComponent
import com.tromian.game.afproject.data.db.MoviesDB
import com.tromian.game.afproject.data.db.checkAdultContent
import com.tromian.game.afproject.data.db.getPosterUrl
import com.tromian.game.afproject.data.db.toActor
import com.tromian.game.afproject.data.db.toGenre
import com.tromian.game.afproject.data.db.toGenreEntity
import com.tromian.game.afproject.data.db.toMovie
import com.tromian.game.afproject.data.db.toMovieEntity
import com.tromian.game.afproject.data.db.toTmdbType
import com.tromian.game.afproject.data.network.models.JsonMovie
import com.tromian.game.afproject.data.network.tmdbapi.ResponseWrapper
import com.tromian.game.afproject.data.network.tmdbapi.TmdbAPI
import com.tromian.game.afproject.data.paging.MoviePagingSource
import com.tromian.game.afproject.domain.MovieListType
import com.tromian.game.afproject.domain.Resource
import com.tromian.game.afproject.domain.models.Actor
import com.tromian.game.afproject.domain.models.Genre
import com.tromian.game.afproject.domain.models.Movie
import com.tromian.game.afproject.domain.repository.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesDataRepository @Inject constructor(
    private val service: TmdbAPI,
    private val localDB: MoviesDB,
    private val context: Application
) : MoviesRepository {

    private var genres: List<Genre>? = null

    @Inject
    lateinit var pagingSourceFactory: MoviePagingSource.Factory

    init {
        context.appComponent.inject(this)
        if (genres == null) {
            CoroutineScope(Dispatchers.IO).launch {
                getMovieGenreList()
            }
        }
    }

    override fun getMovieListResultStream(listType: MovieListType): Flow<PagingData<Movie>> {
        return if (ResponseWrapper.isNetworkConnected(context)) {
            Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
                pagingSourceFactory = { pagingSourceFactory.create(listType.toTmdbType()) }
            ).flow
                .map { pagingData ->
                    pagingData.map { movie -> movie.toMovie() }
                }
        } else flowOf(PagingData.empty())

    }


    override suspend fun getFavouriteMovieListFromDB(): List<Movie> {
        return localDB.movieDao().getFavourite().map {
            it.toMovie()
        }
    }

    private suspend fun getMovieGenreList() {
        val localGenres = localDB.genreDao().getGenreList()
        if (localGenres.isNotEmpty()) {
            genres = localGenres.map {
                it.toGenre()
            }
        } else {
            val remoteGenres = getAllGenreListFromApi()
            genres = remoteGenres
            localDB.genreDao().insertListGenre(remoteGenres.map {
                it.toGenreEntity()
            })
        }
    }

    override suspend fun getMovieCastsByIdInApi(movieId: Int): List<Actor> {
        return if (ResponseWrapper.isNetworkConnected(context)) {
            val result = ResponseWrapper.safeApiResponse(service.getCredits(movieId))
            when (result) {
                is Resource.Success ->
                    if (result.data.actorList == null) {
                        emptyList()
                    } else {
                        result.data.actorList.toActor()
                    }
                is Resource.Error -> {
                    emptyList()
                }
            }

        } else emptyList()

    }

    override suspend fun getAllGenreListFromApi(): List<Genre> {
        return if (ResponseWrapper.isNetworkConnected(context)) {
            val result = ResponseWrapper.safeApiResponse(service.getGenres())
            when (result) {
                is Resource.Success ->
                    if (result.data.genres == null) {
                        emptyList()
                    } else {
                        result.data.genres.toGenre()
                    }
                is Resource.Error -> {
                    return emptyList()
                }
            }

        } else emptyList()

    }

    override suspend fun getTypedListMoviesWithPage(
        page: Int,
        type: MovieListType
    ): List<Movie> {
        return if (ResponseWrapper.isNetworkConnected(context)) {
            val result = ResponseWrapper.safeApiResponse(
                service.getMovieListByListType(type.toTmdbType())
            )
            when (result) {
                is Resource.Success ->
                    result.data.movieList.toMovie()
                is Resource.Error -> {
                    emptyList()
                }
            }

        } else emptyList()

    }

    override suspend fun saveMovieListToDB(movies: List<Movie>) {
        val entities = movies.map {
            it.toMovieEntity()
        }
        localDB.movieDao().insertMovies(entities)
    }

    override suspend fun getSavedMovieListFromDB(): List<Movie> {
        val movies = localDB.movieDao().getNowPlaying().map {
            it.toMovie()
        }
        return movies
    }

    override suspend fun saveMovieToDB(movie: Movie) {
        movie.toMovieEntity().also {
            localDB.movieDao().insertMovie(it)
        }
    }

    override suspend fun deleteMovieFromDB(movie: Movie) {
        movie.toMovieEntity().also {
            localDB.movieDao().deleteMovie(it)
        }
    }

    override suspend fun searchMoviesByTitleInApi(page: Int, title: String): List<Movie> {
        return if (ResponseWrapper.isNetworkConnected(context)) {
            val result = ResponseWrapper.safeApiResponse(service.search(page, title))
            when (result) {
                is Resource.Success ->
                    result.data.movieList.toMovie()
                is Resource.Error -> {
                    emptyList()
                }
            }
        } else emptyList()
    }

    private fun loadGenres(genreIds: List<Int>): String {
        return if (genres != null) {
            var result = ""
            genreIds.forEach { id ->
                genres!!.forEach { genre ->
                    if (id == genre.id) {
                        result += genre.name + " "
                    }
                }
            }
            result
        } else ""
    }

    private fun JsonMovie.toMovie(): Movie {
        val newId: Int? = this.id
        val newTitle: String? = this.title
        val newOverview: String? = this.overview
        return if (newId != null && newTitle != null && newOverview != null) {
            Movie(
                id = newId,
                title = newTitle,
                genres = this.genreIds?.let { id -> loadGenres(id) },
                imageUrl = getPosterUrl() + this.posterPath,
                reviewCount = this.voteCount,
                pgAge = this.adult?.let { adult -> checkAdultContent(adult) },
                rating = ratingDoubleToInt(this.voteAverage),
                storyLine = newOverview
            )
        } else {
            Movie(
                id = newId ?: 10000,
                title = newTitle ?: "Title",
                genres = this.genreIds?.let { id -> loadGenres(id) },
                imageUrl = getPosterUrl() + this.posterPath,
                reviewCount = this.voteCount,
                pgAge = this.adult?.let { adult -> checkAdultContent(adult) },
                rating = ratingDoubleToInt(this.voteAverage),
                storyLine = newOverview ?: "nothing"
            )
        }
    }

    private fun List<JsonMovie>.toMovie(): List<Movie> {
        val movies = mutableListOf<Movie>()
        this.forEach {
            val newId: Int? = it.id
            val newTitle: String? = it.title
            val newOverview: String? = it.overview
            if (newId != null && newTitle != null && newOverview != null) {
                movies.add(
                    Movie(
                        id = newId,
                        title = newTitle,
                        genres = it.genreIds?.let { id -> loadGenres(id) },
                        imageUrl = getPosterUrl() + it.posterPath,
                        reviewCount = it.voteCount,
                        pgAge = it.adult?.let { adult -> checkAdultContent(adult) },
                        rating = ratingDoubleToInt(it.voteAverage),
                        storyLine = newOverview
                    )
                )
            }
        }
        return movies
    }

    private fun ratingDoubleToInt(tmdbRating: Double?) = tmdbRating?.div(2)?.toInt()
}