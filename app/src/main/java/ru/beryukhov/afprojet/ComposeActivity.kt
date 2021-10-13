package ru.beryukhov.afprojet

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tromian.game.afproject.R
import com.tromian.game.afproject.appComponent
import com.tromian.game.afproject.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject
import ru.beryukhov.afprojet.film_details.FilmPage
import ru.beryukhov.afprojet.film_list.MoviesPage

class ComposeActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: ViewModelFactory.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContent {
            PagesContent(factory.create())
        }
    }
}

/*@Preview()
@Preview(device = Devices.PIXEL_C)
@Composable
fun DefaultPreview() {
    PagesContent()
}*/

//val films = List(32) { index -> FILM.copy(age = index.toString()) }

@Composable
fun isShowRail(): Boolean {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    return screenWidth>screenHeight
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PagesContent(viewModelFactory: ViewModelFactory) {
    var filmState: Film? by remember { mutableStateOf(null) }
    MyTheme {
        Scaffold(backgroundColor = colorResource(id = R.color.background)) {
            Row {
                if (isShowRail()) {
                    NavigationRail(
                        backgroundColor = colorResource(id = R.color.background_card),
                        contentColor = colors.primary
                    ) {
                        NavigationRailItem(selected = filmState == null,
                            onClick = {
                                filmState = null
                            },
                            label = { Text(stringResource(id = R.string.menu_main_list)) },
                            icon = {
                                Icon(
                                    painterResource(id = R.drawable.ic_main_list_menu),
                                    contentDescription = "",
                                )
                            }
                        )
                        NavigationRailItem(selected = filmState != null,
                            onClick = {
                                filmState = FILM
                            },
                            label = { Text(stringResource(id = R.string.menu_search)) },
                            icon = {
                                Icon(
                                    painterResource(id = R.drawable.ic_search_menu),
                                    contentDescription = "",
                                )
                            }
                        )
                        NavigationRailItem(selected = filmState != null,
                            onClick = {
                                filmState = FILM
                            },
                            label = { Text(stringResource(id = R.string.menu_liked_movies)) },
                            icon = {
                                Icon(
                                    painterResource(id = R.drawable.ic_liked_menu),
                                    contentDescription = "",
                                )
                            }
                        )
                    }
                }
                Column {
                    when (filmState) {
                        null -> MoviesPage(
                            viewModelFactory = viewModelFactory,
                            tempNavigationCallback = { filmState = it }
                        )
                        else -> FilmPage(film = filmState!!)
                    }
                    if (!isShowRail()) {
                        BottomNavigation(
                            backgroundColor = colorResource(id = R.color.background_card),
                            contentColor = colors.primary
                        ) {
                            BottomNavigationItem(selected = filmState == null,
                                onClick = {
                                    filmState = null
                                },
                                label = { Text(stringResource(id = R.string.menu_main_list)) },
                                icon = {
                                    Icon(
                                        painterResource(id = R.drawable.ic_main_list_menu),
                                        contentDescription = "",
                                    )
                                }
                            )
                            BottomNavigationItem(selected = filmState != null,
                                onClick = {
                                    filmState = FILM
                                },
                                label = { Text(stringResource(id = R.string.menu_search)) },
                                icon = {
                                    Icon(
                                        painterResource(id = R.drawable.ic_search_menu),
                                        contentDescription = "",
                                    )
                                }
                            )
                            BottomNavigationItem(selected = filmState != null,
                                onClick = {
                                    filmState = FILM
                                },
                                label = { Text(stringResource(id = R.string.menu_liked_movies)) },
                                icon = {
                                    Icon(
                                        painterResource(id = R.drawable.ic_liked_menu),
                                        contentDescription = "",
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
