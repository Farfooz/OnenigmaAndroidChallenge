package com.onenigma.pokemonapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onenigma.pokemonapp.databinding.PokemonListFragmentBinding
import com.onenigma.pokemonapp.databinding.PokemonListItemBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PokemonListFragment : Fragment() {
    private lateinit var fragmentBinding: PokemonListFragmentBinding
    private lateinit var layoutManager: LinearLayoutManager

    private val viewModel: PokemonListFragmentViewModel by viewModels()
    private val adapter = PokemonListFragmentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = PokemonListFragmentBinding.inflate(inflater)
        layoutManager = fragmentBinding.recyclerView.layoutManager as LinearLayoutManager
        fragmentBinding.recyclerView.adapter = adapter

        fragmentBinding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            ).apply {
                setDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.pokemon_list_divider
                    )!!
                )
            })

        fragmentBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findLastVisibleItemPosition()
                    > viewModel.pokemonData.value.size - 50
                ) {
                    viewModel.fetchMorePokemon()
                }
            }
        })

        viewModel.fetchMorePokemon()

        viewModel.pokemonData.onEach {
            if (it.isEmpty()) {
                fragmentBinding.spinner.visibility = View.VISIBLE
                fragmentBinding.recyclerView.visibility = View.GONE
            } else {
                fragmentBinding.spinner.visibility = View.GONE
                fragmentBinding.recyclerView.visibility = View.VISIBLE
            }

            adapter.items = it
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        return fragmentBinding.root
    }

    inner class PokemonListFragmentAdapter : RecyclerView.Adapter<PokemonListItemViewHolder>() {
        var items: List<PokemonListItemData> = emptyList()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): PokemonListItemViewHolder {
            return PokemonListItemViewHolder(
                PokemonListItemBinding.inflate(
                    LayoutInflater.from(
                        context
                    ), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: PokemonListItemViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }

    inner class PokemonListItemViewHolder(private val binding: PokemonListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var data: PokemonListItemData

        init {
            binding.container.setOnClickListener {
                this@PokemonListFragment.requireActivity()
                    .supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.container, PokemonDetailFragment.create(data.url))
                        addToBackStack(null)
                    }
            }
        }

        fun bind(data: PokemonListItemData) {
            binding.number.text = "#${data.number}"
            binding.name.text = data.name
            this.data = data
        }
    }
}