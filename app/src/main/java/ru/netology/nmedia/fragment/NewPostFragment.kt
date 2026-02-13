package ru.netology.nmedia.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val binding = FragmentNewPostBinding.inflate(layoutInflater)
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)
        /*
                val initialText = requireActivity(). intent.getStringExtra(Intent.EXTRA_TEXT).orEmpty()
                binding.content.setText(initialText)
                binding.content.setSelection(binding.content.text?.length ?: 0)

                binding.save.setOnClickListener {
                    val text = binding.content.text.toString()
                    if (!binding.content.text.isNullOrBlank()) {
                        viewModel.save(text)
                    }

         */

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id != 0L) {
                binding.content.setText(post.content)
                binding.content.setSelection(binding.content.text?.length ?: 0)
            } else {
                val initialText =
                    requireActivity().intent.getStringExtra(Intent.EXTRA_TEXT).orEmpty()
                if (initialText.isNotBlank()) {
                    binding.content.setText(initialText)
                    binding.content.setSelection(binding.content.text?.length ?: 0)
                    requireActivity().intent.removeExtra(Intent.EXTRA_TEXT)
                }
            }
        }

        binding.save.setOnClickListener {
            val text = binding.content.text.toString().trim()
            if (text.isNotBlank()) {
                viewModel.save(text)
                findNavController().navigateUp()
            }
//        возможно добавить проверку на пустой текст, показать ошибку
        }

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                viewModel.cancelEditing()
            }
        })
//            else {
////                val intent = Intent().apply {
////                    putExtra(Intent.EXTRA_TEXT, text)
////                }
////                setResult(RESULT_OK, intent)
////                val content = binding.content.text.toString()
//                viewModel.save(text)
//            }
//            finish()
//        findNavController().navigateUp()
//    }
        return binding.root
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val binding = ActivityNewPostBinding.inflate(layoutInflater)
//
//        val initialText = intent.getStringExtra(Intent.EXTRA_TEXT).orEmpty()
//        binding.content.setText(initialText)
//        binding.content.setSelection(binding.content.text?.length ?: 0)
//
//        binding.save.setOnClickListener {
//            val text = binding.content.text.toString()
//            if (text.isBlank()) {
//                setResult(RESULT_CANCELED)
//            } else {
//                val intent = Intent().apply {
//                    putExtra(Intent.EXTRA_TEXT, text)
//                }
//                setResult(RESULT_OK, intent)
//            }
//            finish()
//        }
//    }
}