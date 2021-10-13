package com.tromian.game.afproject.presentation.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tromian.game.afproject.R
import com.tromian.game.afproject.appComponent
import com.tromian.game.afproject.domain.MovieListType
import com.tromian.game.afproject.presentation.view.adapters.MoviePagingAdapter
import com.tromian.game.afproject.presentation.viewmodels.MoviesViewModel
import com.tromian.game.afproject.presentation.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {

    private var listType = MovieListType.POPULAR
    private lateinit var tv_list_title: TextView

    @Inject
    lateinit var factory: ViewModelFactory.Factory

    private val viewModel by viewModels<MoviesViewModel> {
        factory.create(listType = listType)
    }

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        MoviePagingAdapter { itemId ->
            openFragment(itemId)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_list_title = view.findViewById(R.id.tv_movie_list_title)
        tv_list_title.text = setListTitleByType(listType)
        val menuImage: ImageView = view.findViewById(R.id.iv_list_type_popup)
        menuImage.setOnClickListener {
            showPopupMenu(menuImage)
        }

        val rvMovieList = view.findViewById<RecyclerView>(R.id.rvMovieList)
        rvMovieList.adapter = adapter
        lifecycleScope.launchWhenStarted {
            viewModel.loadMovieList(listType)
                .collectLatest {
                    adapter.submitData(it)
                }
        }

    }

    private fun openFragment(itemId: Int) {
        val movie = adapter.snapshot().items[itemId]
        Log.d("go", "${this.javaClass.name} + ${movie.javaClass.name}")
        val action = FragmentMoviesListDirections
            .actionFragmentMoviesListToFragmentMoviesDetails(movie)
        findNavController().navigate(action)


    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.list_type_popup_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            menuItemClicked(menuItem)
        }
        popupMenu.show()
    }

    private fun setListTitleByType(type: MovieListType): String {
        return when (type) {
            MovieListType.NOW_PLAYING -> getString(R.string.item_now_playing)
            MovieListType.TOP_RATED -> getString(R.string.item_top_rated)
            MovieListType.POPULAR -> getString(R.string.item_popular)
            MovieListType.UPCOMING -> getString(R.string.item_upcoming)
        }
    }

    private fun menuItemClicked(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_now_playing -> {
                listType = MovieListType.NOW_PLAYING
                listBySelectedTypeUpdated(listType)
            }
            R.id.item_popular -> {
                listType = MovieListType.POPULAR
                listBySelectedTypeUpdated(listType)
            }
            R.id.item_top_rated -> {
                listType = MovieListType.TOP_RATED
                listBySelectedTypeUpdated(listType)
            }
            R.id.item_upcoming -> {
                listType = MovieListType.UPCOMING
                listBySelectedTypeUpdated(listType)
            }
            else -> false
        }

    }

    private fun listBySelectedTypeUpdated(listType: MovieListType): Boolean {
        tv_list_title.setText(setListTitleByType(listType))
        lifecycleScope.launch {
            viewModel.loadMovieList(listType)
                .collectLatest {
                    adapter.submitData(it)
                }
        }
        return true
    }

}