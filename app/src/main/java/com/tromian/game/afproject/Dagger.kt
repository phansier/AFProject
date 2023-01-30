package com.tromian.game.afproject


import android.app.Application
import android.util.Log
import com.tromian.game.afproject.data.db.MoviesDB
import com.tromian.game.afproject.data.network.tmdbapi.TmdbAPI
import com.tromian.game.afproject.data.repository.MoviesDataRepository
import com.tromian.game.afproject.domain.repository.MoviesRepository
import com.tromian.game.afproject.presentation.view.fragments.FragmentFavourite
import com.tromian.game.afproject.presentation.view.fragments.FragmentMoviesDetails
import com.tromian.game.afproject.presentation.view.fragments.FragmentMoviesList
import com.tromian.game.afproject.presentation.view.fragments.FragmentSearch
import dagger.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import ru.beryukhov.afprojet.ComposeActivity

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    fun inject(fragment: FragmentMoviesList)
    fun inject(fragment: FragmentMoviesDetails)
    fun inject(fragment: FragmentSearch)
    fun inject(fragment: FragmentFavourite)

    fun inject(activity: ComposeActivity)

    @Component.Factory
    interface Builder {

        fun create(
            @BindsInstance appContext: Application
        ): AppComponent

    }

}


@Module(includes = [NetworkModule::class, AppBindModule::class, LocalDBModule::class])
class AppModule


@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideTmdbService(appContext: Application): TmdbAPI {
        val authInterceptor = Interceptor { chain ->
            val newUrl = chain.request().url
                .newBuilder()
                .addQueryParameter("api_key", AppConstants.TMDB_API_KEY)
                .addQueryParameter(
                    "language",
                    appContext.resources.getString(R.string.api_language)
                )
                .build()

            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)

        }

        val logger = HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger { Log.d("API", it) }
        ).apply {
            this.level = HttpLoggingInterceptor.Level.BASIC
        }

        val tmdbClient = OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logger)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .client(tmdbClient)
            .baseUrl(AppConstants.BASE_URL_TMDB)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(TmdbAPI::class.java)
    }
}

@Module
class LocalDBModule {
    @Provides
    @Singleton
    fun provideLocalDB(appContext: Application): MoviesDB {
        return MoviesDB.getInstance(appContext)
    }

}

@Module
interface AppBindModule {

    @Binds
    fun bindMoviesRepoImpl_to_MoviesRepo(
        moviesRepoImpl: MoviesDataRepository
    ): MoviesRepository

}