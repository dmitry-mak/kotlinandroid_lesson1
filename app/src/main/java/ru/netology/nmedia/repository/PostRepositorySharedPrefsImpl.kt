package ru.netology.nmedia.repository

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositorySharedPrefsImpl(context: Context) : PostRepository {

    private val prefs = context.getSharedPreferences("postsRepository", Context.MODE_PRIVATE)
    private var nextId = 1L
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            sync()
        }
    private val data = MutableLiveData(posts)

    //    операция чтения
    init {
        prefs.getString(KEY_POSTS, null)?.let {
            posts = gson.fromJson(it, token)
            nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
            data.value = posts
        }
    }

    //    операция записи
    private fun sync() {
        prefs.edit {
            putString(KEY_POSTS, gson.toJson(posts))
        }
    }

    override fun getData(): LiveData<List<Post>> = data

    override fun like(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                isLiked = !it.isLiked,
                likesCount = if (it.isLiked) it.likesCount - 1 else it.likesCount + 1
            )
        }
        data.value = posts
    }

    override fun share(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                sharesCount = it.sharesCount + 1
            )
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(post.copy(id = nextId++)) + posts
        } else {
            posts.map { if (it.id != post.id) it else it.copy(content = post.content) }
        }
        data.value = posts
    }

    companion object {
        private const val KEY_POSTS = "posts"

        private val gson = Gson()

        private val token = TypeToken.getParameterized(List::class.java, Post::class.java).type
    }
}