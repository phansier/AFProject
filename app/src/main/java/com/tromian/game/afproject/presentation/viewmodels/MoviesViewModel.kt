package com.tromian.game.afproject.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.tromian.game.afproject.domain.MovieListType
import com.tromian.game.afproject.domain.models.Movie
import com.tromian.game.afproject.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.beryukhov.afprojet.Film
import ru.beryukhov.afprojet.film_details.actorsList

class MoviesViewModel(
    private val repository: MoviesRepository
) : ViewModel() {

    fun loadMovieList(listType: MovieListType): StateFlow<PagingData<Movie>> {
        return repository.getMovieListResultStream(listType).toStateFlow()
    }

    fun loadFilmList(listType: MovieListType): Flow<PagingData<Film>> =
        loadMovieList(listType).map { it ->
            it.map {
                with(it) {
                    Film(
                        title = title,
                        imageUrl = imageUrl,
                        age = pgAge?.let { "$it+" }.orEmpty(),
                        genres = genres.orEmpty(),
                        rate = rating ?: 0,
                        reviews = "$reviewCount reviews",
                        storyLine = storyLine,
                        actors = actorsList /*TODO*/
                    )
                }
            }
        }

    private fun Flow<PagingData<Movie>>.toStateFlow() = this.cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

}
