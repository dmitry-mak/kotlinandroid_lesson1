package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyInsets(binding)

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

        val post = Post(
            id = 1L,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "14 January, 17:46",
            content = "ПриветТТ, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likesCount = getString(R.string.likesCount).toInt(),
            sharesCount = getString(R.string.shareCount).toInt()
        )

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

            likeIcon.setOnClickListener {
                post.isLiked = !post.isLiked
                likeIcon.setImageResource(
                    if (post.isLiked) {
                        post.likesCount++
                        R.drawable.ic_heartred_24
                    } else {
                        if (post.likesCount > 0) post.likesCount--
                        R.drawable.heart24
                    }
                )
                likesCount.text = DiffMethods.convertNumber(post.likesCount)
            }

            shareCount.text = DiffMethods.convertNumber(post.sharesCount)

            shareIcon.setOnClickListener {
                post.sharesCount++
                shareCount.text = DiffMethods.convertNumber(post.sharesCount)
            }
        }
    }
}