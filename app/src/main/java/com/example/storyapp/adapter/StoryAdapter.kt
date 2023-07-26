package com.example.storyapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.dataClass.StoryItem
import com.example.storyapp.databinding.CardstoryBinding
import com.example.storyapp.ui.DetailActivity

class StoryAdapter(private val listStory : ArrayList<StoryItem>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    class ViewHolder(var binding : CardstoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardstoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storyData = listStory[position]
        fun bind(story : StoryItem){
            holder.binding.apply {
                tvItemName.text = story.name
                tvDescription.text = story.description
                imgItemPhoto.setImageResource(R.drawable.avatar)
                Glide.with(holder.itemView.context)
                    .load(story.photoUrl)
                    .into(imgItemStory)
            }
            holder.itemView.setOnClickListener {
                val intentDetail = Intent(holder.itemView.context,DetailActivity::class.java)
                intentDetail.putExtra("STORY",story)
                holder.itemView.context.startActivity(intentDetail)
            }
        }
        bind(storyData)
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

}