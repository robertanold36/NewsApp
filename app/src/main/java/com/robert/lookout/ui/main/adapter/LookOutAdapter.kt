package com.robert.lookout.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.robert.lookout.R
import com.robert.lookout.ui.main.model.Article
import kotlinx.android.synthetic.main.lookout_news.view.*

class LookOutAdapter : RecyclerView.Adapter<LookOutAdapter.LookOutViewHolder>() {

    inner class LookOutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    private val differCallBack = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LookOutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lookout_news, parent, false)
        return LookOutViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: LookOutViewHolder, position: Int) {
        val result = differ.currentList[position]

        val requestOption = RequestOptions().apply {
            placeholder(R.drawable.loading)

        }

        holder.itemView.apply {
            news_title.text = result.title
            news_date.text = result.publishedAt
            news_publisher.text = result.source?.name
            news_description.text = result.description
            Glide.with(this).setDefaultRequestOptions(requestOption).load(result.urlToImage)
                .into(news_img)

            setOnClickListener {
                onItemClickListener?.let {
                    it(result)
                }
            }

        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}