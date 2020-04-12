package com.ppb.kwid.Model.Transaction

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ppb.kwid.Activity.PurchaseConfirmationActivity
import com.ppb.kwid.R

class TransactionHistoryAdapter(
    private val transactions: MutableList<Transaction>,
    private var context: Context
) :
    RecyclerView.Adapter<TransactionHistoryAdapter.TransactionHistoryListHolder>() {

    inner class TransactionHistoryListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val moviePoster: ImageView = itemView.findViewById(R.id.img_movie_poster)
        private val movieTitle: TextView = itemView.findViewById(R.id.txt_movie_title)
        private val cinemaLocation: TextView = itemView.findViewById(R.id.txt_cinema_location)
        private val sumTicket: TextView = itemView.findViewById(R.id.txt_sum_ticket)
        private val dateAndTime: TextView = itemView.findViewById(R.id.txt_date)

        fun bind(transaction: Transaction) {
            Glide.with(itemView)
                .load(transaction.movie_poster)
                .centerCrop()
                .into(moviePoster)

            movieTitle.text = transaction.movie_title

            cinemaLocation.text = transaction.cinema_name

            val sumTicketText = "Ticket(" + transaction.picked_seat.size.toString() + ")"
            sumTicket.text = sumTicketText

            val dateAndTimeText = transaction.schedule + ", " + transaction.time
            dateAndTime.text = dateAndTimeText

            itemView.setOnClickListener {
                val intent = Intent(context, PurchaseConfirmationActivity::class.java)
                intent.putExtra(PurchaseConfirmationActivity.EXTRA_TRANSACTION, transaction)
                intent.putExtra(PurchaseConfirmationActivity.EXTRA_IS_HISTORY, true)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionHistoryListHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_transaction_history, parent, false)

        return TransactionHistoryListHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionHistoryListHolder, position: Int) {
        holder.bind(transactions[position])
    }
}