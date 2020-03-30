package com.ppb.kwid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

class CastAdapter(
    private var casts: List<Cast>
) : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastAdapter.CastViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_cast, parent, false)
        return CastViewHolder(view)
    }

    override fun getItemCount(): Int = casts.size

    override fun onBindViewHolder(holder: CastAdapter.CastViewHolder, position: Int) {
        holder.bind(casts[position])
    }

    fun updateCasts(casts: List<Cast>) {
        this.casts = casts
        notifyDataSetChanged()
    }

    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val poster: ImageView = itemView.findViewById(R.id.item_cast_poster)
        private var tvCastName: TextView = itemView.findViewById(R.id.item_cast_name)
        private var tvCastRole: TextView = itemView.findViewById(R.id.item_cast_character)

        fun bind(cast: Cast) {

            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${cast.profilePath}")
                .transform(CenterCrop())
                .into(poster)

            tvCastName.text = cast.name
            tvCastRole.text = cast.character

        }
    }
}
