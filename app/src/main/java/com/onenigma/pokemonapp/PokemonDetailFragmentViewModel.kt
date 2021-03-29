package com.onenigma.pokemonapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonDetailFragmentViewModel : ViewModel() {
    private var job: Job? = null

    private val _pokemonFlow: MutableStateFlow<Pokemon?> = MutableStateFlow(null)
    val pokemonFlow: StateFlow<Pokemon?> = _pokemonFlow

    fun fetchPokemon(url: String): Unit {
        if (job?.isActive == true) {
            return
        }

        job = viewModelScope.launch {
            _pokemonFlow.value = PokemonAPI.instance().getPokemonDetail(url)
        }
    }
}