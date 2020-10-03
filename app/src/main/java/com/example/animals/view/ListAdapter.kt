package com.example.animals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.animals.R
import com.example.animals.databinding.ItemAnimalBinding
import com.example.animals.model.Animal

class ListAdapter(private var animalList: ArrayList<Animal>) :
    RecyclerView.Adapter<ListAdapter.AnimalViewHolder>(), OnClickListener {

    class AnimalViewHolder(var view: ItemAnimalBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemAnimalBinding>(
            inflater,
            R.layout.item_animal,
            parent,
            false
        )
        return AnimalViewHolder(view)
    }

    override fun getItemCount() = animalList.size

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.view.animal = animalList[position]
        holder.view.listener = this
    }

    fun updateAnimalList(newAnimalList: List<Animal>) {
        animalList.clear()
        animalList.addAll(newAnimalList)
        notifyDataSetChanged()
    }

    override fun onClick(view: View) {
        for (animal in animalList) {
            if (view.tag == animal.name) {
                val action = ListFragmentDirections.actionDetail(animal)
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

}