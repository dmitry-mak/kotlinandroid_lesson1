package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PostAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupObservers()
        setupListeners()
        applyInsets()
    }


    private fun applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    private fun setupAdapter() {
        adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.share(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }
        })
        binding.list.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.data.observe(this) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }
        viewModel.edited.observe(this) { post ->
            if (post.id != 0L) {
                binding.editGroup.visibility = View.VISIBLE
                binding.content.setText(post.content)
                binding.content.requestFocus()
                AndroidUtils.showKeyboard(binding.content)
            } else {
                binding.editGroup.visibility = View.GONE
                binding.content.setText("")
                binding.content.clearFocus()
                AndroidUtils.hideKeyboard(binding.content)
            }
        }
    }

    private fun setupListeners() {
        binding.save.setOnClickListener {
            with(binding.content) {
                val text = text.toString()
                if (text.isBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.empty_notificaton,
                        Toast.LENGTH_LONG
                    )
                        .show()
                    return@setOnClickListener
                }
                viewModel.save(text)
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(binding.content)
            }
        }
        binding.editCancelButton.setOnClickListener {
            viewModel.cancelEditing()
            binding.content.setText("")
            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(binding.content)
        }
    }
}
