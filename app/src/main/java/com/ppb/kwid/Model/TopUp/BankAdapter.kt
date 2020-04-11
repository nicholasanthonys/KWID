package com.ppb.kwid.Model.TopUp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ppb.kwid.R
import de.hdodenhof.circleimageview.CircleImageView

class BankAdapter(private var banks: MutableList<Bank>) :
    RecyclerView.Adapter<BankAdapter.BankListHolder>() {

    inner class BankListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bankImageView: CircleImageView = itemView.findViewById(R.id.bank_image)
        private val bankTextView: TextView = itemView.findViewById(R.id.bank_name)
        private val btnNext: ImageView = itemView.findViewById(R.id.btn_navigate_next)

        fun bind(bank: Bank) {
            bankTextView.text = bank.name

            Glide.with(itemView)
                .load(bank.image_url)
                .centerCrop()
                .into(bankImageView)

            btnNext.setOnClickListener {
                val webIntent: Intent = Uri.parse(bank.url).let { webpage ->
                    Intent(Intent.ACTION_VIEW, webpage)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankListHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_transfer_bank, parent, false)

        return BankListHolder(view)
    }

    override fun getItemCount(): Int {
        return banks.size
    }

    override fun onBindViewHolder(holder: BankListHolder, position: Int) {
        holder.bind(banks[position])
    }
}