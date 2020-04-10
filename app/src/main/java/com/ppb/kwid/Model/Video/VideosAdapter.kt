package com.ppb.kwid.Model.Video

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.ppb.kwid.R

class VideosAdapter(
    private var videosResponses: MutableList<VideosResponse>,
    private var context: Context
) : RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun getItemCount(): Int = videosResponses.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videosResponses[position])
    }

    fun updateVideo(videoResponse: VideosResponse) {
        videosResponses.add(videoResponse)
        notifyDataSetChanged()
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var videoPoster: ImageView = itemView.findViewById(R.id.video_poster)
        private var btnPlay: Button = itemView.findViewById(R.id.btn_play_video_home)
        fun bind(videoResponse: VideosResponse) {

            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${videoResponse.backdropPath}")
                .transform(CenterCrop())
                .into(videoPoster)

            btnPlay.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + videoResponse.videos.videoResults[0].key)
                )
                context.startActivity(intent)
            }

        }
    }
}
