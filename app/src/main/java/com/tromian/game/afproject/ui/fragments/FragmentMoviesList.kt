package com.tromian.game.afproject.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tromian.game.afproject.AppConstants
import com.tromian.game.afproject.R
import com.tromian.game.afproject.ui.adapters.MovieListAdapter
import com.tromian.game.afproject.ui.MainActivity
import com.tromian.game.afproject.viewmodels.MoviesViewModel

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {

    lateinit var viewModel: MoviesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(AppConstants.LOG,"start onViewCreated in FragmentMoviesList")
        viewModel = (activity as MainActivity).moviesViewModel


        val adapter = MovieListAdapter() { itemId ->
            openFragment(itemId)
        }

        viewModel.movieList.observe(requireActivity(), Observer {
            adapter.submitList(it)
        })

        val rvMovieList = view.findViewById<RecyclerView>(R.id.rvMovieList)

        rvMovieList.adapter = adapter
        rvMovieList.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)

    }

    private fun openFragment(itemId: Int) {
        val movie = viewModel.movieList.value?.get(itemId)
        val bundle = Bundle()
        bundle.putInt("ItemId",itemId)
        findNavController().navigate(R.id.fragmentMoviesDetails,bundle)
//        val activity = requireActivity() as MainActivity
//        if (movie != null) {
//            activity.supportFragmentManager
//                    .beginTransaction()
//                    .addToBackStack(null)
//                    .add(R.id.main_container, FragmentMoviesDetails(), MainActivity.FRAGMENT_DETAIL)
//                    .commit()
//        }

    }


}