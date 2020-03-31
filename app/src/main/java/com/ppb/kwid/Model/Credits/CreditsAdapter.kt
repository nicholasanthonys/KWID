package com.ppb.kwid.Model.Credits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.ppb.kwid.R

class CreditsAdapter(
    private var casts: List<Cast>,
    private var crews : List<Crew>
) : RecyclerView.Adapter<CreditsAdapter.CastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_cast, parent, false)
        return CastViewHolder(view)
    }

    override fun getItemCount(): Int = casts.size

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(casts[position])
    }

    fun updateCasts(casts: List<Cast>, crews : List<Crew>) {
        this.casts = casts
        this.crews = crews
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
