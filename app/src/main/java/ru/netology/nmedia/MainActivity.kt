package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyInsets(binding)

        viewModel.data.observe(this) { post ->
            with(binding) {
                postContent.text = post.content
                author.text = post.author
                publishDay.text = post.published
                likeIcon.setImageResource(
                    if (post.isLiked) {
                        R.drawable.ic_heartred_24
                    } else {
                        R.drawable.heart24
                    }
                )

                likesCount.text = DiffMethods.convertNumber(post.likesCount)
                shareCount.text = DiffMethods.convertNumber(post.sharesCount)
            }
        }

        binding.likeIcon.setOnClickListener {
            viewModel.like()
        }

        binding.shareIcon.setOnClickListener {
            viewModel.share()
        }

    }

    private fun applyInsets(binding: ActivityMainBinding) {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft + systemBars.left,
                v.paddingTop + systemBars.top,
                v.paddingRight + systemBars.right,
                v.paddingBottom + systemBars.bottom
            )
            insets
        }
    }
}
