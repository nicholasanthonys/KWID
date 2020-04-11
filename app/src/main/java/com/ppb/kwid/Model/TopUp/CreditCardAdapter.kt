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

class CreditCardAdapter(private var creditCards: MutableList<CreditCard>) :
    RecyclerView.Adapter<CreditCardAdapter.CreditCardListHolder>() {

    inner class CreditCardListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val creditCardImageView: ImageView = itemView.findViewById(R.id.credit_card_image)
        private val creditCardTextView: TextView = itemView.findViewById(R.id.credit_card_name)
        private val btnNext: ImageView = itemView.findViewById(R.id.btn_navigate_next)

        fun bind(creditCard: CreditCard) {
            creditCardTextView.text = creditCard.name

            Glide.with(itemView)
                .load(creditCard.image_url)
                .centerCrop()
                .into(creditCardImageView)

            btnNext.setOnClickListener {
                val webIntent: Intent = Uri.parse(creditCard.url).let { webpage ->
                    Intent(Intent.ACTION_VIEW, webpage)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardListHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_credit_card, parent, false)

        return CreditCardListHolder(view)
    }

    override fun getItemCount(): Int {
        return creditCards.size
    }

    override fun onBindViewHolder(holder: CreditCardListHolder, position: Int) {
        holder.bind(creditCards[position])
    }
}