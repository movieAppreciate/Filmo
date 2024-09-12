package com.teamfilmo.filmo.data.remote.model.movie

data class Providers(
    val buy: List<Buy>,
    val link: String,
    val rent: List<Rent>,
)
