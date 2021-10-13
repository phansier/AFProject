package com.tromian.game.afproject.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tromian.game.afproject.domain.MovieListType
import com.tromian.game.afproject.domain.repository.MoviesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ViewModelFactory @AssistedInject constructor(
    @Assisted("listType") private val listType: MovieListType = MovieListType.POPULAR,
    @Assisted("movieId") private val movieId: Int = 0,
    private val repository: MoviesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MoviesViewModel::class.java -> {
                MoviesViewModel(repository)
            }
            MovieSearchVM::class.java -> {
                MovieSearchVM(repository)
            }
            MovieDetailsViewModel::class.java -> {
                MovieDetailsViewModel(movieId, repository)
            }
            FavouriteViewModel::class.java -> {
                FavouriteViewModel(repository)
            }
            else -> throw IllegalStateException("Unknown view model class")
        }
        return viewModel as T
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("movieId") movieId: Int = 0,
            @Assisted("listType") listType: MovieListType = MovieListType.POPULAR
        ): ViewModelFactory
    }
}
