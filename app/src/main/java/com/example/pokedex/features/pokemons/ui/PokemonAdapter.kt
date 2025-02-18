package com.example.pokedex.features.pokemons.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.databinding.ItemPokemonBinding
import com.example.pokedex.features.pokemons.data.Pokemon

class PokemonAdapter : ListAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(PokemonDiffCallback()) {
    private val currentList = mutableListOf<Pokemon>() // Mantiene la lista actual de Pokémon

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = currentList[position]
        holder.bind(pokemon)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    /**
     * Agrega más Pokémon a la lista y notifica al adapter
     */
    fun addPokemons(newPokemons: List<Pokemon>) {
        val startIndex = currentList.size
        currentList.addAll(newPokemons)
        notifyItemRangeInserted(startIndex, newPokemons.size) // Notifica solo los nuevos elementos
    }

    inner class PokemonViewHolder(private val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: Pokemon) {
            binding.textViewPokemonName.text = pokemon.name
            Glide.with(binding.imageViewPokemon.context)
                .load(pokemon.sprites?.image) // Usa la URL de la imagen del Pokémon
                .placeholder(com.example.pokedex.R.drawable.pokemon) // Imagen de carga
                .into(binding.imageViewPokemon)
        }
    }

    class PokemonDiffCallback : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }
    }
}
