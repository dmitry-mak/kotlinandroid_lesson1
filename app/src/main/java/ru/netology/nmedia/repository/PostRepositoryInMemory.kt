package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemory : PostRepository {
    private var nextId = 4L
    private var posts = listOf(
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "14 January, 17:46",
            content = "ПриветТТ, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likesCount = 10,
            sharesCount = 9999
        ),
        Post(
            id = 2,
            author = "Нетология. Университет вообще всех профессий",
            published = "27 January, 17:46",
            content = "Тут какой-то случайный текст",
            likesCount = 999,
            sharesCount = 9999
        ),
        Post(
            id = 3,
            author = "А тут вот будет другой автор",
            published = "27 January, 17:46",
            content = "Небо над портом напоминало телеэкран, включенный на мертвый канал.\n" +
                    "– Разве же я употребляю? – услышал Кейс, продираясь сквозь толпу к «Тацу». – Просто у моего организма острая а********о-н***********я недостаточность.\n" +
                    "И голос рожденного в Муравейнике, и шуточка муравьиная. В «Тацубо», где собирались все больше спецы-экспаты, можно просидеть неделю и слова не услышать по-японски.",
            likesCount = 9999,
            sharesCount = 999999
        ),
        Post(
            id = 4,
            author = "И еще один автор",
            published = "25 January, 17:46",
            content = "Пенфилдовский генератор настроений разбудил Рика Декарда звонким, радостным всплеском электричества. Привычно удивленный неожиданным, без всяких предупреждений, возвращением из царства сна в мир реальный, Рик спрыгнул с кровати, одернул радужную, под стать своему утреннему настроению, пижаму и сладко, с хрустом потянулся.",
            likesCount = 9999,
            sharesCount = 99999
        )
    )
    private val data = MutableLiveData(posts)

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
}