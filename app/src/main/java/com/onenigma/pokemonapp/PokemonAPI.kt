package com.onenigma.pokemonapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonAPI {
    @GET("pokemon")
    suspend fun getPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponse

    @GET
    suspend fun getPokemonDetail(@Url url: String): Pokemon

    companion object {
        fun instance(): PokemonAPI =
            Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(PokemonAPI::class.java)
    }
}