package com.clover.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clover.rickandmorty.databinding.RowLayoutBinding
import com.clover.rickandmorty.model.Character
import java.net.URI

class MainAdapter(
    private val onItemClicked: (String, Int) -> Unit
): RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    private var characters = emptyList<Character>()

    class DataViewHolder(private val binding: RowLayoutBinding,
                         onItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener{
                onItemClicked(bindingAdapterPosition)
            }
        }

        fun bind(character: Character) {
            itemView.apply {
                binding.name.text = character.name
                binding.status.text = character.status
                binding.species.text = character.species
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) {
            onItemClicked(characters[it].image, getLocationId(characters[it].url))
        }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    fun setData(characters: List<Character>) {
        this.characters = characters
        notifyDataSetChanged()
    }

    private fun getLocationId(locationUrl: String): Int {
        val path: String = URI(locationUrl).path
        return path.substring(path.lastIndexOf('/') + 1).toInt()
    }
}