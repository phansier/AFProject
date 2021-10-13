package ru.beryukhov.afprojet

import ru.beryukhov.afprojet.film_details.Actor
import ru.beryukhov.afprojet.film_details.actorsList

data class Film(
    val imageUrl: String?= null,
    val title: String,
    val age: String,
    val genres: String,
    val rate: Int,
    val reviews: String,
    val storyLine: String,
    val actors: List<Actor>,
)

private const val storylineString =
    "After the devastating events of Avengers: Infinity War, the universe is in ruins. " +
            "With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' " +
            "actions and restore balance to the universe."

val FILM = Film(
    imageUrl = null,
    title = "Avengers: End Game",
    age = "13+",
    genres = "Action, Adventure, Fantasy",
    rate = 4,
    reviews = "123 Review",
    storyLine = storylineString,
    actors = actorsList + actorsList,
)