package com.abhishek.internships.identifier.skysnap.adpater

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.internships.identifier.skysnap.databinding.ItemCityBinding
import com.abhishek.internships.identifier.skysnap.model.Current

class CityAdapter(
    var list: MutableList<Current>? = null,
    var context : Context
) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {



    fun submitList(newList: List<Current>) {
        list?.clear()
        list?.addAll(newList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemCityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = list?.get(position)?.wind_speed_10m
//        holder.binding.textView.text =
//            "${item} : C"
        Log.d("TAG", "onBindViewHolder: $item")
    }


    override fun getItemCount(): Int {
        return list?.count() ?: 0
    }

    class ViewHolder(
        val binding : ItemCityBinding
    ) : RecyclerView.ViewHolder(binding.root)

}
