package com.ppb.kwid.Model.Video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.ppb.kwid.R

class VideosAdapter(
    private var results: MutableList<Result>,
    private var backdrop: String
) : RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(results[position])
    }

    fun updateVideo(results: MutableList<Result>, backdrop: String) {
        this.results = results
        this.backdrop = backdrop
        notifyDataSetChanged()
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvVideoUrl: TextView = itemView.findViewById(R.id.tes)
        private var videoPoster: ImageView = itemView.findViewById(R.id.video_poster)
        fun bind(result: Result) {

            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${backdrop}")
                .transform(CenterCrop())
                .into(videoPoster)

            tvVideoUrl.text = result.key

        }
    }
}
