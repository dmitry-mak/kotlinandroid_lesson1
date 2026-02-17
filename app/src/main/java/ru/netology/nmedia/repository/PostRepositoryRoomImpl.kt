package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryRoomImpl(
    private val dao: PostDao
) : PostRepository {
//    private var posts = emptyList<Post>()
//    private val data = MutableLiveData(posts)

//    init {
//        posts = dao.getAll()
//        data.value = posts
//    }

    override fun getData(): LiveData<List<Post>> = dao.getAll().map { list ->
        list.map { it.toDto() }
    }

    override fun like(id: Long) {
//        dao.likeById(id)
//        posts = posts.map {
//            if (it.id != id) it else it.copy(
//                isLiked = !it.isLiked,
//                likesCount = if (it.isLiked) it.likesCount - 1 else it.likesCount + 1
//            )
//        }
//        data.value = posts
        dao.likeById(id)
    }

    override fun share(id: Long) {
//        dao.share(id)
//        posts = posts.map {
//            if (it.id != id) it else it.copy(sharesCount = it.sharesCount + 1)
//        }
//        data.value = posts
        dao.share(id)
    }

    override fun removeById(id: Long) {
//        dao.removeById(id)
//        posts = posts.filter { it.id != id }
//        data.value = posts
        dao.removeById(id)
    }

    override fun save(post: Post) {
//        val id = post.id
//        val saved = dao.save(post)
//        posts = if (id == 0L) {
//            listOf(saved) + posts
//        } else {
//            posts.map {
//                if (it.id != id) it else saved
//            }
//        }

//        data.value = posts
        dao.save(PostEntity.fromDto(post))
    }
}