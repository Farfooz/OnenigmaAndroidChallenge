package com.onenigma.pokemonapp

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.onenigma.pokemonapp.databinding.PokemonDetailsFragmentBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

class PokemonDetailFragment : Fragment() {
    companion object {
        private val ARG_URL = "url"
        fun create(url: String): PokemonDetailFragment = PokemonDetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_URL, url)
            }
        }
    }

    private lateinit var fragmentBinding: PokemonDetailsFragmentBinding
    private val viewModel: PokemonDetailFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = PokemonDetailsFragmentBinding.inflate(inflater)

        viewModel.fetchPokemon(requireArguments().getString(ARG_URL)!!)
        viewModel.pokemonFlow.onEach {
            if (it == null) {
                fragmentBinding.spinner.visibility = VISIBLE
                fragmentBinding.image.visibility = GONE
                fragmentBinding.type1.visibility = GONE
                fragmentBinding.type2.visibility = GONE
                fragmentBinding.name.visibility = GONE
                return@onEach
            }

            fragmentBinding.spinner.visibility = GONE
            fragmentBinding.image.visibility = VISIBLE
            fragmentBinding.type1.visibility = VISIBLE
            fragmentBinding.type2.visibility = VISIBLE
            fragmentBinding.name.visibility = VISIBLE

            fragmentBinding.name.text = it.species.name.capitalize(Locale.ENGLISH)
            Picasso.get().load(it.sprites.front_default).into(fragmentBinding.image)

            val typeColor = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    it.types[0].type.asTypeColor()!!.colorRes
                )
            )

            fragmentBinding.image.backgroundTintList = typeColor
            fragmentBinding.type1.backgroundTintList = typeColor
            fragmentBinding.type1.text = it.types[0].type.name.toUpperCase()


            if (it.types.size > 1) {
                fragmentBinding.type2.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        it.types[1].type.asTypeColor()!!.colorRes
                    )
                )
                fragmentBinding.type2.text = it.types[1].type.name.toUpperCase()
            } else {
                fragmentBinding.type2.visibility = GONE
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        return fragmentBinding.root
    }
}