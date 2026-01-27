package ru.netology.nmedia.adapter

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.DiffMethods
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post


typealias OnLikeListener = (post: Post) -> Unit
typealias OnShareListener = (post: Post) -> Unit


class PostAdapter(
    private var onLikeListener: OnLikeListener,
    private var onShareListener: OnShareListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding, onLikeListener, onShareListener)
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            publishDay.text = post.published
            postContent.text = post.content
            likesCount.text = DiffMethods.convertNumber(post.likesCount)
            likeIcon.setImageResource(
                if (post.isLiked) {
                    R.drawable.ic_heartred_24
                } else {
                    R.drawable.heart24
                }
            )
            likeIcon.setOnClickListener {
                onLikeListener(post)
            }
            shareIcon.setOnClickListener { onShareListener(post) }
            shareCount.text = DiffMethods.convertNumber(post.sharesCount)
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Post,
        newItem: Post
    ): Boolean {
        return oldItem == newItem
    }
}



