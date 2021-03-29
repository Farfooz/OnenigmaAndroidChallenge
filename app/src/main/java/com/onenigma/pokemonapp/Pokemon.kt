package com.onenigma.pokemonapp

data class Pokemon(
    val species: PokemonSpecies,
    val sprites: PokemonSprites,
    val types: List<PokemonTypeSlot>
)