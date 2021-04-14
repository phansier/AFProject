package com.tromian.game.afproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tromian.game.afproject.R
import com.tromian.game.afproject.SomeItemClickListener
import com.tromian.game.afproject.model.repository.MoviesRepository
import com.tromian.game.afproject.ui.fragments.FragmentMoviesDetails
import com.tromian.game.afproject.ui.fragments.FragmentMoviesList
import com.tromian.game.afproject.viewmodels.MovieDetailsVMFactory
import com.tromian.game.afproject.viewmodels.MovieDetailsViewModel
import com.tromian.game.afproject.viewmodels.MoviesViewModel
import com.tromian.game.afproject.viewmodels.MoviesViewModelProviderFactory

class MainActivity : AppCompatActivity(R.layout.activity_main), SomeItemClickListener {

    companion object {
        const val FRAGMENT_LIST = "List"
        const val FRAGMENT_DETAIL = "Detail"

    }

    lateinit var moviesViewModel: MoviesViewModel
    lateinit var movieDetailsViewModel: MovieDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = MoviesRepository()
        val viewModelFactory = MoviesViewModelProviderFactory(application, repository)
        val detailViewModelFactory = MovieDetailsVMFactory(application, repository)
        moviesViewModel = ViewModelProvider(this, viewModelFactory).get(MoviesViewModel::class.java)
        movieDetailsViewModel = ViewModelProvider(this,detailViewModelFactory).get(MovieDetailsViewModel::class.java)


//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .add(R.id.main_container, FragmentMoviesList(), FRAGMENT_LIST)
//                    .commit()
//
//        } else if (savedInstanceState.containsKey(FRAGMENT_DETAIL)){
//            supportFragmentManager.findFragmentByTag(FRAGMENT_DETAIL) as FragmentMoviesDetails
//        }else{
//            supportFragmentManager.findFragmentByTag(FRAGMENT_LIST) as FragmentMoviesList
//        }

    }

    override fun onBackButtonClicked() {
        supportFragmentManager.popBackStack()
    }


}