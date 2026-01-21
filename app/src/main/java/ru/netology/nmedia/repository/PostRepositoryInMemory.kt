package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemory : PostRepository {
    var post = Post(
        id = 1L,
        author = "Нетология. Университет интернет-профессий будущего",
        published = "14 January, 17:46",
        content = "ПриветТТ, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        likesCount = 10,
        sharesCount = 9999

    )
    private val data = MutableLiveData(post)

    override fun getData(): LiveData<Post> = data

    override fun like() {
        post = post.copy(
            isLiked = !post.isLiked,
            likesCount = if (post.isLiked) {
                post.likesCount - 1
            } else {
                post.likesCount + 1
            }
        )
        data.value = post
    }

    override fun share() {
        post = post.copy(
            sharesCount = post.sharesCount + 1
        )
        data.value = post

    }
}