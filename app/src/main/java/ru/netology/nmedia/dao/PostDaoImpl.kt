package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post


class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {


    companion object {
        val DDL = """
        CREATE TABLE ${PostColumns.TABLE_NAME} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKES_COUNT} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_IS_LIKED} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARES_COUNT} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIDEO} TEXT   
                );
    """.trimIndent()
    }

    object PostColumns {
        const val TABLE_NAME = "posts"
        const val COLUMN_ID = "_id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKES_COUNT = "likes_count"
        const val COLUMN_IS_LIKED = "is_liked"
        const val COLUMN_SHARES_COUNT = "shares_count"
        const val COLUMN_VIDEO = "video"

        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_LIKES_COUNT,
            COLUMN_IS_LIKED,
            COLUMN_SHARES_COUNT,
            COLUMN_VIDEO
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE_NAME,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostColumns.COLUMN_AUTHOR, "Netology")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_PUBLISHED, "15 February, 17:46")
            put(PostColumns.COLUMN_VIDEO, post.video)
        }
        val id = if (post.id != 0L) {
            db.update(
                PostColumns.TABLE_NAME,
                values,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(post.id.toString()),
            )
            post.id
        } else {
            db.insert(PostColumns.TABLE_NAME, null, values)
        }
        db.query(
            PostColumns.TABLE_NAME,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
                UPDATE ${PostColumns.TABLE_NAME} SET
                likes_count = likes_count + CASE WHEN is_liked = 1 THEN -1 ELSE 1 END,
                is_liked = CASE WHEN is_liked = 1 THEN 0 ELSE 1 END
                WHERE ${PostColumns.COLUMN_ID} = ?;
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE_NAME,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun share(id: Long) {
        db.execSQL(
            """
                UPDATE ${PostColumns.TABLE_NAME} SET
                ${PostColumns.COLUMN_SHARES_COUNT} = ${PostColumns.COLUMN_SHARES_COUNT} + 1
                WHERE ${PostColumns.COLUMN_ID} = ?;
                """.trimIndent(), arrayOf(id)
        )
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                likesCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES_COUNT)),
                isLiked = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_IS_LIKED)) != 0,
                sharesCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARES_COUNT)),
                video = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO))
            )
        }
    }

}