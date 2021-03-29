package com.onenigma.pokemonapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class PokemonListFragmentViewModel : ViewModel() {
    private var currentJob: Job? = null
    private val PAGE_SIZE = 100
    private var currentOffset = 0

    private val _pokemonData: MutableStateFlow<List<PokemonListItemData>> =
        MutableStateFlow(emptyList())
    val pokemonData: StateFlow<List<PokemonListItemData>> = _pokemonData

    fun fetchMorePokemon() {
        if (currentJob?.isActive == true) {
            return
        }

        currentJob = viewModelScope.launch {
            val pokemon = PokemonAPI.instance().getPokemon(PAGE_SIZE, currentOffset)
            if (pokemon.results.isEmpty()) {
                return@launch
            }

            val mutableList = _pokemonData.value.toMutableList()
            mutableList.addAll((0 until (pokemon.results.size)).map {
                PokemonListItemData(
                    pokemon.results[it].name.split("-").joinToString(" ") { str ->
                        str.capitalize(Locale.ENGLISH)
                    },
                    pokemon.results[it].url,
                    it + currentOffset + 1
                )
            })

            currentOffset += PAGE_SIZE
            _pokemonData.value = mutableList
        }
    }
}