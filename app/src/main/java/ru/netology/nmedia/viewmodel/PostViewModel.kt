package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemory

private val empty = Post(
    id = 0,
    author = "Netology",
    published = "02 February 20:34",
    content = "",
    likesCount = 0,
    sharesCount = 0,
    isLiked = false
)

class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemory()

    val data = repository.getData()
    val edited = MutableLiveData(empty)

    fun like(id: Long) {
        repository.like(id)
    }

    fun share(id: Long) {
        repository.share(id)
    }

    fun removeById(id: Long) {
        repository.removeById(id)
    }

    fun save(text: String) {
        edited.value?.let {
            if (it.content != text) {
                repository.save(it.copy(content = text.trim()))
            }
        }
        edited.value = empty
    }

    fun edit(post: Post){
        edited.value = post
    }

    fun cancelEditing(){
        edited.value = empty
        }
}