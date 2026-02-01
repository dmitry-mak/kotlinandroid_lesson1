package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    //    Использую lateinit, чтобы не инициализировать var binding и var adapter в
    //    момент объявления, а отложить  инициализацию до момента вызова onCreate.
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PostAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupObservers()
        applyInsets()

//        val viewModel: PostViewModel by viewModels()
//        val adapter = PostAdapter(
//            onLikeListener = { post -> viewModel.like(post.id) },
//            onShareListener = { post -> viewModel.share(post.id) }
//        )
//
//        binding.list.adapter = adapter
//        viewModel.data.observe(this) { posts ->
//            adapter.submitList(posts)
//        }
    }

    private fun applyInsets() {
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

    private fun setupAdapter() {
        adapter = PostAdapter(
            onLikeListener = { post -> viewModel.like(post.id) },
            onShareListener = { post -> viewModel.share(post.id) }
        )
        binding.list.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }
}
