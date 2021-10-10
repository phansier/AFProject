package com.tromian.game.afproject.presentation.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tromian.game.afproject.GlideApp
import com.tromian.game.afproject.R
import com.tromian.game.afproject.domain.models.Actor

class ActorsListAdapter(private val context: Context) :
        ListAdapter<Actor, ActorsListAdapter.ActorsViewHolder>(DIFF_CALLBACK) {


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Actor>() {
            override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean =
                    oldItem == newItem
        }
    }


    inner class ActorsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val avatar: ImageView = itemView.findViewById(R.id.avatar)
        private val name: TextView = itemView.findViewById(R.id.name)


        fun bind(actor: Actor) {

            GlideApp.with(itemView.context)
                    .load(actor.imageUrl)
                    .error(R.drawable.person_placeholder)
                    .into(avatar)

            name.text = actor.name
        }
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ActorsViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        return ActorsViewHolder(inflater.inflate(R.layout.item_actor, parent, false))
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}